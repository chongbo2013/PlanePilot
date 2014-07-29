//incoming Position attribute from our SpriteBatch
/**attribute vec2 Position;
attribute vec4 Color;
attribute vec2 TexCoord0;
uniform vec2 u_projection;
varying vec2 vTexCoord0;
varying vec4 vColor;

void main(void) {
   gl_Position = vec4( Position.x / u_projection.x - 1.0, Position.y / -u_projection.y + 1.0 , 0.0, 1.0);
   vTexCoord0 = TexCoord0;
   vColor = Color;
}
**/
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0; // first texture co-ord

uniform mat4 u_projTrans;

varying vec4 vColor;
varying vec2 vTexCoord0;

void main() {
	vColor = a_color;
	vTexCoord0 = a_texCoord0;
	gl_Position = u_projTrans * a_position;	 // standard passthrough
}