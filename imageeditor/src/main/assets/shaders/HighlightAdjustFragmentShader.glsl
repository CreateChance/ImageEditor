uniform sampler2D u_InputTexture;
varying highp vec2 v_TextureCoordinates;

uniform lowp float u_Highlight;

const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);

// 用于亮度 luminance 的计算, copy from GPUImage GPUImageSaturationFilter.m
// https://github.com/BradLarson/GPUImage/blob/master/framework/Source/GPUImageSaturationFilter.m
const vec4 kLuminanceWeighting = vec4(0.2125, 0.7154, 0.0721, 0.0);
float getLuminance(vec4 color)
{
    return dot(color, kLuminanceWeighting);
}

vec4 highlightAdjust(vec4 color)
{
    float luminance = getLuminance(color);
    float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-u_Highlight)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-u_Highlight)))) - luminance, -1.0, 0.0);
    vec3 rgb = vec3(0.0, 0.0, 0.0) + (luminance + highlight) * ((color.rgb - vec3(0.0))/(luminance - 0.0));

    return vec4(rgb, color.a);
}

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = highlightAdjust(textureColor);
}