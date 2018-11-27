/**
 * 3 x 3 sampling fragment shader.
 * Giving 3 x 3 kernel to sample color.
 * Copy from GPUImage: https://github.com/BradLarson/GPUImage/blob/master/framework/Source/GPUImage3x3ConvolutionFilter.m
 */

precision highp float;

uniform sampler2D u_InputTexture;

uniform mediump mat3 u_SampleKernel;

varying vec2 v_TextureCoordinates;
varying vec2 v_LeftTextureCoordinate;
varying vec2 v_RightTextureCoordinate;

varying vec2 v_TopTextureCoordinate;
varying vec2 v_TopLeftTextureCoordinate;
varying vec2 v_TopRightTextureCoordinate;

varying vec2 v_BottomTextureCoordinate;
varying vec2 v_BottomLeftTextureCoordinate;
varying vec2 v_BottomRightTextureCoordinate;

void main()
{
    mediump vec3 bottomColor = texture2D(u_InputTexture, v_BottomTextureCoordinate).rgb;
    mediump vec3 bottomLeftColor = texture2D(u_InputTexture, v_BottomLeftTextureCoordinate).rgb;
    mediump vec3 bottomRightColor = texture2D(u_InputTexture, v_BottomRightTextureCoordinate).rgb;
    mediump vec4 centerColor = texture2D(u_InputTexture, v_TextureCoordinates);
    mediump vec3 leftColor = texture2D(u_InputTexture, v_LeftTextureCoordinate).rgb;
    mediump vec3 rightColor = texture2D(u_InputTexture, v_RightTextureCoordinate).rgb;
    mediump vec3 topColor = texture2D(u_InputTexture, v_TopTextureCoordinate).rgb;
    mediump vec3 topRightColor = texture2D(u_InputTexture, v_TopRightTextureCoordinate).rgb;
    mediump vec3 topLeftColor = texture2D(u_InputTexture, v_TopLeftTextureCoordinate).rgb;

    mediump vec3 resultColor = topLeftColor * u_SampleKernel[0][0] + topColor * u_SampleKernel[0][1] + topRightColor * u_SampleKernel[0][2];
    resultColor += leftColor * u_SampleKernel[1][0] + centerColor.rgb * u_SampleKernel[1][1] + rightColor * u_SampleKernel[1][2];
    resultColor += bottomLeftColor * u_SampleKernel[2][0] + bottomColor * u_SampleKernel[2][1] + bottomRightColor * u_SampleKernel[2][2];

    gl_FragColor = vec4(resultColor, centerColor.a);
}