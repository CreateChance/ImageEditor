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

vec4 shadowAdjust(vec4 color)
{
    float luminance = getLuminance(color);
    float shadow = clamp((pow(luminance, 1.0/(u_Shadow+1.0)) + (-0.76)*pow(luminance, 2.0/(u_Shadow+1.0))) - luminance, 0.0, 1.0);
    vec3 rgb = vec3(0.0, 0.0, 0.0) + (luminance + shadow) * ((color.rgb - vec3(0.0))/(luminance - 0.0));

    return vec4(rgb, color.a);
}

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = shadowAdjust(textureColor);
}