varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform lowp vec2 u_VignetteCenter;
uniform lowp vec3 u_VignetteColor;
uniform highp float u_VignetteStart;
uniform highp float u_VignetteEnd;

void main()
{
    lowp vec3 rgb = texture2D(u_InputTexture, v_TextureCoordinates).rgb;
    lowp float d = distance(v_TextureCoordinates, vec2(u_VignetteCenter.x, u_VignetteCenter.y));
    lowp float percent = smoothstep(u_VignetteStart, u_VignetteEnd, d);
    gl_FragColor = vec4(mix(rgb.x, u_VignetteColor.x, percent), mix(rgb.y, u_VignetteColor.y, percent), mix(rgb.z, u_VignetteColor.z, percent), 1.0);
}