
#ifdef GL_ES
	precision highp float;
#endif


/// Uniform variables.
uniform sampler2D u_texture0;   //orig map
uniform sampler2D u_texture1;   //glow map
uniform int BlendMode;

/// Varying variables.
varying vec2 vTexCoord0;
varying vec2 vColor;

void main ()
{
	vec4 dst = texture2D(u_texture0, vTexCoord0); // rendered scene
	vec4 src = texture2D(u_texture1, vTexCoord0); // glowmap

//    if(src.a == 0.0){
//        discard;
//    }

	if ( BlendMode == 0 )
	{
		// Additive blending (strong result, high overexposure)
		gl_FragColor = min(src + dst, 1.0);
	}
	else if ( BlendMode == 1 )
	{
		// Screen blending (mild result, medium overexposure)
		gl_FragColor = clamp((src + dst) - (src * dst), 0.0, 1.0);
		gl_FragColor.w = 1.0;
	}
	else if ( BlendMode == 2 )
	{
		// Softlight blending (light result, no overexposure)
		// Due to the nature of soft lighting, we need to bump the black region of the glowmap
		// to 0.5, otherwise the blended result will be dark (black soft lighting will darken
		// the image).
		src = (src * 0.5) + 0.5;
		
		gl_FragColor.xyz = vec3((src.x <= 0.5) ? (dst.x - (1.0 - 2.0 * src.x) * dst.x * (1.0 - dst.x)) : (((src.x > 0.5) && (dst.x <= 0.25)) ? (dst.x + (2.0 * src.x - 1.0) * (4.0 * dst.x * (4.0 * dst.x + 1.0) * (dst.x - 1.0) + 7.0 * dst.x)) : (dst.x + (2.0 * src.x - 1.0) * (sqrt(dst.x) - dst.x))),
								(src.y <= 0.5) ? (dst.y - (1.0 - 2.0 * src.y) * dst.y * (1.0 - dst.y)) : (((src.y > 0.5) && (dst.y <= 0.25)) ? (dst.y + (2.0 * src.y - 1.0) * (4.0 * dst.y * (4.0 * dst.y + 1.0) * (dst.y - 1.0) + 7.0 * dst.y)) : (dst.y + (2.0 * src.y - 1.0) * (sqrt(dst.y) - dst.y))),
								(src.z <= 0.5) ? (dst.z - (1.0 - 2.0 * src.z) * dst.z * (1.0 - dst.z)) : (((src.z > 0.5) && (dst.z <= 0.25)) ? (dst.z + (2.0 * src.z - 1.0) * (4.0 * dst.z * (4.0 * dst.z + 1.0) * (dst.z - 1.0) + 7.0 * dst.z)) : (dst.z + (2.0 * src.z - 1.0) * (sqrt(dst.z) - dst.z))));
		gl_FragColor.w = 1.0;
	}
	else
	{
		// Show just the glow map
		gl_FragColor = src;
	}
}