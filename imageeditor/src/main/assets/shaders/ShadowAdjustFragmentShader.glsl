varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform float u_Shadow;

// 用于亮度 luminance 的计算, copy from GPUImage GPUImageSaturationFilter.m
// https://github.com/BradLarson/GPUImage/blob/master/framework/Source/GPUImageSaturationFilter.m
const vec4 kLuminanceWeighting = vec4(0.2125, 0.7154, 0.0721, 0.0);
float getLuminance(vec4 color)
{
    return dot(color, kLuminanceWeighting);
}

vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec4 shadowAdjust()
{
    vec4 color = texture2D(u_InputTexture, v_TextureCoordinates);
    float luminance = rgb2hsv(color.rgb).z;
    float shadow = clamp((pow(luminance, 1.0/(u_Shadow+1.0)) + (-0.76)*pow(luminance, 2.0/(u_Shadow+1.0))) - luminance, 0.0, 1.0);
    vec3 rgb = vec3(0.1, 0.1, 0.1) + (luminance + shadow) * ((color.rgb - vec3(0.1))/(luminance - 0.0));

    return vec4(rgb, color.a);
}

vec4 shadowAdjust2() {
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    vec4 leftColor = texture2D(u_InputTexture, vec2(v_TextureCoordinates.x - 0.001, v_TextureCoordinates.y));
    vec4 topColor = texture2D(u_InputTexture, vec2(v_TextureCoordinates.x, v_TextureCoordinates.y + 0.001));
    vec4 rightColor = texture2D(u_InputTexture, vec2(v_TextureCoordinates.x + 0.001, v_TextureCoordinates.y));
    vec4 bottomColor = texture2D(u_InputTexture, vec2(v_TextureCoordinates.x, v_TextureCoordinates.y - 0.001));
    vec3 centerHsvColor = rgb2hsv(textureColor.rgb);
    vec3 leftHsvColor = rgb2hsv(leftColor.rgb);
    vec3 topHsvColor = rgb2hsv(topColor.rgb);
    vec3 rightHsvColor = rgb2hsv(rightColor.rgb);
    vec3 bottomHsvColor = rgb2hsv(bottomColor.rgb);
    if ((centerHsvColor.z + leftHsvColor.z + topHsvColor.z + rightHsvColor.z + bottomHsvColor.z) < 1.5) {
        centerHsvColor = vec3(centerHsvColor.xy, centerHsvColor.z * pow(1.3, u_Shadow));
    }

    return vec4(hsv2rgb(centerHsvColor), textureColor.a);
}

void main()
{
    gl_FragColor = shadowAdjust();
}