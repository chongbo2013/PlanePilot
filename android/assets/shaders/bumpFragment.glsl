//attributes from vertex shader
varying vec4 vColor;
varying vec2 vTexCoord0;

//our texture samplers
uniform sampler2D u_texture;   //diffuse map
uniform sampler2D u_normals;   //normal map

uniform int lightCount;         //number of lights (constant)

//values used for shading algorithm...
uniform vec2 Resolution;      //resolution of screen
uniform vec3 LightsPos[32];   // Max of 32 Lights - TODO: add max to §global config
uniform vec4 LightColor;      //light RGBA -- alpha is intensity
uniform vec4 AmbientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 Falloff;         //attenuation coefficients

void main() {

    // todo: Abide by Max Lights
    // Hard Code Max - for Now, Until We integrate this GLSL Program one into a SpriteBatcher
    const int maxLights = 32;

    // Store our Sum of lights on the Render
    vec3 Sum = vec3(0.0, 0.0, 0.0);

    //RGBA of our diffuse color
    vec4 DiffuseColor = texture2D(u_texture, vTexCoord0);

    //RGB of our normal map
    vec3 NormalMap = texture2D(u_normals, vTexCoord0).rgb;

    for (int i=0; i<lightCount; i++) {

        //The delta position of light
        vec3 LightDir = vec3(LightsPos[i].xy - (gl_FragCoord.xy / Resolution.xy), LightsPos[i].z);

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
        //vec3 Diffuse = vec3(vColor);

        //pre-multiply ambient color with intensity
        vec3 Ambient = AmbientColor.rgb * AmbientColor.a;

        //calculate attenuation
        float Attenuation = 1.0 / ( Falloff.x + (Falloff.y * D) + (Falloff.z * D * D) );

        //the calculation which brings it all together
        vec3 Intensity = Diffuse * Attenuation; // drop ambient
        vec3 FinalColor = DiffuseColor.rgb * Intensity;

        Sum += FinalColor;
    }

    // With or Without Lights, the Ambient Color will be consistent
    Sum += DiffuseColor.rgb * 1.0;
    gl_FragColor = vColor * vec4(Sum, DiffuseColor.a);
}