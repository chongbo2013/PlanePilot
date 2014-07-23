//attributes from vertex shader
varying vec4 vColor;
varying vec2 vTexCoord0;

//our texture samplers
uniform sampler2D u_texture;   //diffuse map
uniform sampler2D u_normals;   //normal map

uniform int lightCount;         //number of lights (constant)
//values used for shading algorithm...
uniform vec2 Resolution;      //resolution of screen
uniform vec3 LightPos;        //light position, normalized
uniform vec3 LightPoss[32];   // how does the Passed in float array get converted to a Vec3?
uniform vec4 LightColor;      //light RGBA -- alpha is intensity
uniform vec4 AmbientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 Falloff;         //attenuation coefficients
//uniform int lightCount;         //number of lights (constant)

//const int maxLights = 32;

// TODO: we need to supporte vec3 arrays to push multiple light sources into here
void main() {
    const int maxLights = 32;
    // Store our Sum of lights on the Render
    vec3 Sum = vec3(0.0, 0.0, 0.0);

    //RGBA of our diffuse color
    vec4 DiffuseColor = texture2D(u_texture, vTexCoord0);

    //RGB of our normal map
    vec3 NormalMap = texture2D(u_normals, vTexCoord0).rgb;

    //for(int i=0;i<lightCount;i++){
        //The delta position of light
        vec3 LightDir = vec3(LightPos.xy - (gl_FragCoord.xy / Resolution.xy), LightPos.z);
        //vec3 LightDir = vec3(LightPoss[i].xy - (gl_FragCoord.xy / Resolution.xy), LightPoss[i].z);

        //Correct for aspect ratio
        LightDir.x *= Resolution.x / Resolution.y;

        //Determine distance (used for attenuation) BEFORE we normalize our LightDir
        float D = length(LightDir);

        //normalize our vectors
        vec3 N = normalize(NormalMap * 2.0 - 1.0);
        vec3 L = normalize(LightDir);

        //Pre-multiply light color with intensity
        //Then perform "N dot L" to determine our diffuse term
        vec3 Diffuse = (LightColor.rgb * LightColor.a) * max(dot(N, L), 0.0);

        //pre-multiply ambient color with intensity
        vec3 Ambient = AmbientColor.rgb * AmbientColor.a;

        //calculate attenuation
        float Attenuation = 1.0 / ( Falloff.x + (Falloff.y*D) + (Falloff.z*D*D) );

        //the calculation which brings it all together
        vec3 Intensity = Ambient + Diffuse * Attenuation;
        vec3 FinalColor = DiffuseColor.rgb * Intensity;
        Sum += FinalColor;
    //}
    gl_FragColor = vColor * vec4(Sum, DiffuseColor.a);
    ////gl_FragColor = vColor * vec4(vec3(0.0, 0.0, 0.0), DiffuseColor.a);
    //gl_FragColor = vColor * vec4(FinalColor, DiffuseColor.a);
}