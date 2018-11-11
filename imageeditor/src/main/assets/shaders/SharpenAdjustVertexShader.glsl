attribute vec4 a_Position;
attribute vec4 a_TextureCoordinates;

uniform float u_ImageWidthFactor;
uniform float u_ImageHeightFactor;
uniform float u_Sharpness;

varying vec2 v_TextureCoordinate;
varying vec2 v_LeftTextureCoordinate;
varying vec2 v_RightTextureCoordinate;
varying vec2 v_TopTextureCoordinate;
varying vec2 v_BottomTextureCoordinate;

varying float v_CenterMultiplier;
varying float v_EdgeMultiplier;

void main()
{
    gl_Position = a_Position;
    
    mediump vec2 widthStep = vec2(u_ImageWidthFactor, 0.0);
    mediump vec2 heightStep = vec2(0.0, u_ImageHeightFactor);
    
    v_TextureCoordinate = a_TextureCoordinates.xy;
    v_LeftTextureCoordinate = a_TextureCoordinates.xy - widthStep;
    v_RightTextureCoordinate = a_TextureCoordinates.xy + widthStep;
    v_TopTextureCoordinate = a_TextureCoordinates.xy + heightStep;
    v_BottomTextureCoordinate = a_TextureCoordinates.xy - heightStep;
    
    v_CenterMultiplier = 1.0 + 4.0 * u_Sharpness;
    v_EdgeMultiplier = u_Sharpness;
}