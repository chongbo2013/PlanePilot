
attribute vec4 a_position;
attribute vec4 a_color;     // vertex colour - mostly unused
attribute vec2 a_texCoord0; // first texture co-ord

uniform mat4 u_projTrans;   // projectionTransform

varying vec4 v_color;
varying vec2 v_texCoord0;

void main() {
	gl_Position = u_projTrans * a_position;	 // standard passthrough
	v_color = a_color;
	v_texCoord0 = a_texCoord0;
}