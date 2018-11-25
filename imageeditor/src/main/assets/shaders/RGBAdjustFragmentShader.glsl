varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform highp float u_Red;
uniform highp float u_Green;
uniform highp float u_Blue;

void main()
{
    highp vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = vec4(textureColor.r * u_Red, textureColor.g * u_Green, textureColor.b * u_Blue, 1.0);
}