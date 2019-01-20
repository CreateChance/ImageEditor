precision highp float;

varying highp vec2 v_TextureCoordinate;
varying highp vec2 v_LeftTextureCoordinate;
varying highp vec2 v_RightTextureCoordinate;
varying highp vec2 v_TopTextureCoordinate;
varying highp vec2 v_BottomTextureCoordinate;

varying highp float v_CenterMultiplier;
varying highp float v_EdgeMultiplier;

uniform sampler2D u_InputTexture;

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinate);
    mediump vec3 centerColor = textureColor.rgb;
    mediump vec3 leftTextureColor = texture2D(u_InputTexture, v_LeftTextureCoordinate).rgb;
    mediump vec3 rightTextureColor = texture2D(u_InputTexture, v_RightTextureCoordinate).rgb;
    mediump vec3 topTextureColor = texture2D(u_InputTexture, v_TopTextureCoordinate).rgb;
    mediump vec3 bottomTextureColor = texture2D(u_InputTexture, v_BottomTextureCoordinate).rgb;

    gl_FragColor = vec4((centerColor * v_CenterMultiplier -
        (leftTextureColor * v_EdgeMultiplier +
            rightTextureColor * v_EdgeMultiplier +
            topTextureColor * v_EdgeMultiplier +
            bottomTextureColor * v_EdgeMultiplier)), textureColor.w);
}