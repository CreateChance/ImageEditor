 uniform sampler2D u_InputTexture;
 varying highp vec2 v_TextureCoordinates;

 uniform float u_Tint;

// 用于 hue 和 RGB 转换计算的常量，copy from GPUImage GPUImageWhiteBalanceFilter.m
// YIQ https://zh.wikipedia.org/wiki/YIQ
// Y 亮度（Brightness） I 色彩从橙色到青色 Q 色彩从紫色到黄绿色
const mat3 kRGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);
const mat3 kYIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);
/// RGB to YIQ
vec4 RGBtoYIQ(vec4 rgbColor)
{
    vec3 yiq = kRGBtoYIQ * rgbColor.rgb;

    return vec4(yiq, rgbColor.a);
}

/// YIQ to RGB
vec4 YIQtoRGB(vec4 yiqColor)
{
    vec3 rgb = kYIQtoRGB * yiqColor.rgb;

    return vec4(rgb, yiqColor.a);
}

vec4 tintAdjust(vec4 color)
{
    vec4 yiq = RGBtoYIQ(color);
    yiq.b = clamp(yiq.b + u_Tint*0.5226*0.1, -0.5226, 0.5226);
    return YIQtoRGB(yiq);
}

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = tintAdjust(textureColor);
}