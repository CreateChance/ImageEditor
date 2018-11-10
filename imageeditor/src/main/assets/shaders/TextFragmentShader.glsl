precision mediump float;
uniform sampler2D u_InputTexture;
uniform sampler2D u_Background;
uniform int u_UseBackground;
varying vec2 v_TextureCoordinates;
uniform vec3 u_TextColor;
uniform float u_AlphaFactor;

void main() {
    vec4 sampledColor = vec4(1.0, 1.0, 1.0, texture2D(u_InputTexture, v_TextureCoordinates).a);
    if (u_UseBackground == 1) {
        vec4 backgroundColor = texture2D(u_Background, v_TextureCoordinates);
        gl_FragColor = vec4(backgroundColor.rgb, u_AlphaFactor) * sampledColor;
    } else {
        gl_FragColor = vec4(u_TextColor, u_AlphaFactor) * sampledColor;
    }
}