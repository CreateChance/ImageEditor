/**
 * Transition main fragment shader.
 * author: createchance
 */

precision mediump float;

uniform sampler2D u_InputTexture;
uniform sampler2D u_InputTexture2;
uniform float progress;

varying vec2 v_TextureCoordinates;

vec4 transition (vec2 p);

void main() {
    gl_FragColor = transition(v_TextureCoordinates);
}

vec4 getFromColor(vec2 coordinate) {
    return texture2D(u_InputTexture, vec2(coordinate.x, 1.0 - coordinate.y));
}

vec4 getToColor(vec2 coordinate) {
    return texture2D(u_InputTexture2, vec2(coordinate.x, 1.0 - coordinate.y));
}
