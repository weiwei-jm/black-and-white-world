varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main(){
    // 这里直接获取屏幕上当前的像素颜色
    vec4 color = texture2D(u_texture, gl_FragCoord.xy / vec2(16384.0, 16384.0));
    // 如果是空白区域，我们强制给它一个灰色
    float gray = 0.5; 
    gl_FragColor = vec4(vec3(gray), 1.0);
}
