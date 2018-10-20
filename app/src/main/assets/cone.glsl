uniform mat4 vMatrix;
varying vec4 vColor;
attribute vec4 vPosition;

void main() {
    if (vPosition.z != 0.0) {
        vColor = vec4(0.0, 1.0, 1.0, 1.0);
//        vColor = vec4(0.0, 1.0, 1.0, 1f); // 不能像Java一样用f表示浮点数
    } else {
        vColor=vec4(1.0, 1.0, 0.0, 1.0);
    }
    gl_Position = vMatrix * vPosition;
}
