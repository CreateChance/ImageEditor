uniform sampler2D u_InputTexture;
varying highp vec2 v_TextureCoordinates;

uniform lowp float u_Exposure;

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = vec4(textureColor.rgb * pow(2.0, u_Exposure), textureColor.a);
}