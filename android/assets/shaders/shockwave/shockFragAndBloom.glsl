#version 120

/**
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture0;   // 0
uniform vec2 center;            // Mouse position, normalized 0.0 to 1.0
uniform float time;             // effect elapsed time - is reset to 0 when the effect starts
uniform vec3 shockParams;       // x = width y = height z = threshold

varying vec2 vTexCoord0;
varying vec4 vColor;

// todo: implement array of shocks

void main() {
    vec2 uv = vTexCoord0.xy;
    vec2 texCoord = uv;
    float dist = distance(uv, center);
    if ( (dist <= (time + shockParams.z)) && (dist >= (time - shockParams.z)) )
    {
        float diff = (dist - time);
        float powDiff = 1.0 - pow(abs(diff*shockParams.x), shockParams.y);
        float diffTime = diff  * powDiff;
        vec2 diffUV = normalize(uv - center);
        texCoord = uv + (diffUV * diffTime);
    }
    gl_FragColor = texture2D(u_texture0, texCoord) * vColor;
}
**/
#ifdef GL_ES
#define LOWP lowp
#define MED mediump
precision mediump float;
#else
#define LOWP
#define MED
#endif

varying vec2 v_texCoord0;
varying vec4 v_color;

//uniform MED sampler2D u_texture1;
uniform float BloomIntensity;
uniform float OriginalIntensity;
uniform float time; // effect elapsed time
uniform vec3 shockParams; // 10.0, 0.8, 0.1
uniform vec2 center; // effect center position

uniform MED sampler2D u_texture0;

vec2 diffraction(vec2 uv){
   vec2 differ = uv - center;
   float dist2 = dot(differ, differ);
   vec2 diffUV = differ * inversesqrt(dist2);
   float distance = sqrt(dist2);

   float diff = (distance - time);
   float powDiff = 1.0 - pow(abs(diff*shockParams.x), shockParams.y);
   float diffTime = diff  * powDiff;

   vec2 texCoords = uv +  diffUV * diffTime;

   return (abs(diff) > shockParams.z) ? uv : texCoords;
}

void main()
{
        //calculate biased texture coords
   vec2 texCoords = diffraction(v_texCoord0);
   //normal bloom
   vec4 original = texture2D(u_texture0, texCoords) * OriginalIntensity;
   vec4 bloom = texture2D(u_texture0, v_texCoord0) * BloomIntensity;
   original = original *  (1.0 - bloom);
    gl_FragColor =  original + bloom;
}