varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main(){
    vec4 color = texture2D(u_texture, v_texCoords);
    // 计算灰度
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    gl_FragColor = vec4(vec3(gray), color.a);
}
