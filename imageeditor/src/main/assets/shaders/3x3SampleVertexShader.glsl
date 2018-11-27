/**
 * 3 x 3 sampling vertex shader.
 * Giving 3 x 3 kernel to sample color.
 * Copy from GPUImage: https://github.com/BradLarson/GPUImage/blob/master/framework/Source/GPUImage3x3TextureSamplingFilter.m
 */

attribute vec4 a_Position;
attribute vec4 a_TextureCoordinates;

uniform highp float u_WidthStep;
uniform highp float u_HeightStep;

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
    gl_Position = a_Position;

    vec2 widthStep = vec2(u_WidthStep, 0.0);
    vec2 heightStep = vec2(0.0, u_HeightStep);
    vec2 widthHeightStep = vec2(u_WidthStep, u_HeightStep);
    vec2 widthNegativeHeightStep = vec2(u_WidthStep, -u_HeightStep);

    v_TextureCoordinates = a_TextureCoordinates.xy;
    v_LeftTextureCoordinate = a_TextureCoordinates.xy - widthStep;
    v_RightTextureCoordinate = a_TextureCoordinates.xy + widthStep;

    v_TopTextureCoordinate = a_TextureCoordinates.xy - heightStep;
    v_TopLeftTextureCoordinate = a_TextureCoordinates.xy - widthHeightStep;
    v_TopRightTextureCoordinate = a_TextureCoordinates.xy + widthNegativeHeightStep;

    v_BottomTextureCoordinate = a_TextureCoordinates.xy + heightStep;
    v_BottomLeftTextureCoordinate = a_TextureCoordinates.xy - widthNegativeHeightStep;
    v_BottomRightTextureCoordinate = a_TextureCoordinates.xy + widthHeightStep;
}