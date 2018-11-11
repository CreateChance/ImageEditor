varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform lowp float u_Saturation;

// Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham
const mediump vec3 v_LuminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

void main()
{
   lowp vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
   lowp float luminance = dot(textureColor.rgb, v_LuminanceWeighting);
   lowp vec3 greyScaleColor = vec3(luminance);
   
   gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, u_Saturation), textureColor.w);
    
}