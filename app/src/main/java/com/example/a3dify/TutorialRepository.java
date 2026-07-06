package com.example.a3dify;

import com.example.a3dify.models.Tutorial;
import java.util.ArrayList;
import java.util.List;

/*
 * TutorialRepository
 * ══════════════════════════════════════════════════════════════
 * Single source of truth for all tutorial data in the app.
 *
 * Every fragment and activity reads from here instead of
 * defining their own lists. This means:
 *   - Search in HomeFragment finds the same tutorials as Explore
 *   - Category filtering is consistent everywhere
 *   - Adding a new tutorial only requires changing this one file
 *
 * In a production app this would fetch from a remote API or
 * Firestore. For this version the data is defined here and
 * remains consistent across the whole app.
 * ══════════════════════════════════════════════════════════════
 */
public class TutorialRepository {

    // Singleton
    private static TutorialRepository instance;

    public static TutorialRepository getInstance() {
        if (instance == null) {
            instance = new TutorialRepository();
        }
        return instance;
    }

    private final List<Tutorial> allTutorials;

    private TutorialRepository() {
        allTutorials = new ArrayList<>();
        loadTutorials();
    }

    /*
     * All 20 tutorials defined once.
     * Each has a unique tutorialId used for SQLite progress tracking.
     * Categories match exactly what the Explore screen shows.
     */
    private void loadTutorials() {

        // ── Beginner Basics ──────────────────────────────────────
        allTutorials.add(new Tutorial(
            "🔷", "Welcome to Blender",
            "Beginner Basics", "Beginner", "15 min",
            "Your very first Blender session. Learn to open the app, navigate the 3D viewport, and understand the basic interface panels.",
            "welcome_to_blender"
        ));
        allTutorials.add(new Tutorial(
            "🖱️", "Viewport Navigation",
            "Beginner Basics", "Beginner", "12 min",
            "Master rotating, panning, and zooming in the 3D viewport using the mouse and numpad. The foundation of all Blender work.",
            "viewport_navigation"
        ));
        allTutorials.add(new Tutorial(
            "📦", "Understanding Objects",
            "Beginner Basics", "Beginner", "18 min",
            "Learn what objects are in Blender, how to select them, move them, rotate them, and scale them using the G, R, and S shortcuts.",
            "understanding_objects"
        ));

        // ── 3D Modeling ──────────────────────────────────────────
        allTutorials.add(new Tutorial(
            "🧊", "Intro to 3D Modeling",
            "3D Modeling", "Beginner", "28 min",
            "Enter Edit Mode and learn the fundamental tools — extrude, loop cut, bevel, and inset. Build your first real 3D object from a cube.",
            "intro_to_3d_modeling"
        ));
        allTutorials.add(new Tutorial(
            "🏠", "Modeling a Simple House",
            "3D Modeling", "Beginner", "35 min",
            "A practical beginner project. Model a complete simple house using only basic mesh tools. Perfect for building confidence.",
            "modeling_simple_house"
        ));
        allTutorials.add(new Tutorial(
            "⚙️", "Hard Surface Modeling",
            "3D Modeling", "Intermediate", "52 min",
            "Learn techniques for modeling mechanical objects with sharp edges, bevels, and boolean operations. Model a stylised robot head.",
            "hard_surface_modeling"
        ));
        allTutorials.add(new Tutorial(
            "🔩", "Modifiers Explained",
            "3D Modeling", "Intermediate", "40 min",
            "Deep dive into Blender's modifier stack. Subdivision Surface, Mirror, Array, Solidify, and more. Speed up your workflow dramatically.",
            "modifiers_explained"
        ));

        // ── Animation ────────────────────────────────────────────
        allTutorials.add(new Tutorial(
            "🎬", "Keyframe Animation Basics",
            "Animation", "Beginner", "30 min",
            "Set your first keyframes, use the timeline, and create a simple bouncing ball animation. Understand the Graph Editor.",
            "keyframe_animation_basics"
        ));
        allTutorials.add(new Tutorial(
            "🦴", "Rigging for Beginners",
            "Animation", "Intermediate", "48 min",
            "Build an armature, parent it to a mesh, and weight paint. Create a basic character rig you can actually pose and animate.",
            "rigging_for_beginners"
        ));
        allTutorials.add(new Tutorial(
            "🚶", "Walk Cycle Animation",
            "Animation", "Intermediate", "45 min",
            "Animate a complete human walk cycle from scratch. Learn about timing, spacing, anticipation, and follow-through principles.",
            "walk_cycle_animation"
        ));

        // ── Sculpting ────────────────────────────────────────────
        allTutorials.add(new Tutorial(
            "🌊", "Sculpting Basics",
            "Sculpting", "Beginner", "22 min",
            "Explore sculpt mode, the most important brushes (Draw, Clay, Smooth, Grab), and dynamic topology. Create an organic rock.",
            "sculpting_basics"
        ));
        allTutorials.add(new Tutorial(
            "👤", "Sculpting a Face",
            "Sculpting", "Intermediate", "60 min",
            "Sculpt a human face from a sphere. Learn facial anatomy, proportion guidelines, and how professionals approach character sculpting.",
            "sculpting_a_face"
        ));

        // ── Rendering ────────────────────────────────────────────
        allTutorials.add(new Tutorial(
            "💡", "Lighting Setups",
            "Rendering", "Beginner", "18 min",
            "Three-point lighting, HDRI environments, area lights vs point lights. Set up professional lighting for any scene.",
            "lighting_setups"
        ));
        allTutorials.add(new Tutorial(
            "📷", "Camera and Composition",
            "Rendering", "Beginner", "20 min",
            "Set up cameras, adjust focal length, apply rule of thirds, and use depth of field to make your renders look cinematic.",
            "camera_and_composition"
        ));
        allTutorials.add(new Tutorial(
            "🌅", "Cycles vs EEVEE",
            "Rendering", "Intermediate", "35 min",
            "Understand when to use Cycles for photorealism and EEVEE for speed. Configure render settings for quality and performance.",
            "cycles_vs_eevee"
        ));

        // ── Geometry Nodes ───────────────────────────────────────
        allTutorials.add(new Tutorial(
            "⚙️", "Geometry Nodes Intro",
            "Geometry Nodes", "Intermediate", "45 min",
            "Your first procedural model using Blender's node-based system. Create a scatter system that places objects on a surface.",
            "geometry_nodes_intro"
        ));
        allTutorials.add(new Tutorial(
            "🌿", "Procedural Grass",
            "Geometry Nodes", "Intermediate", "38 min",
            "Build a fully procedural grass and ground cover system using Geometry Nodes. Control density, variation, and wind.",
            "procedural_grass"
        ));

        // ── Materials and Textures ───────────────────────────────
        allTutorials.add(new Tutorial(
            "🎨", "PBR Materials",
            "Materials & Textures", "Intermediate", "35 min",
            "Create physically based render materials using the Shader Editor. Understand Base Color, Roughness, Metallic, and Normal maps.",
            "pbr_materials"
        ));
        allTutorials.add(new Tutorial(
            "🖌️", "UV Unwrapping",
            "Materials & Textures", "Intermediate", "42 min",
            "Learn to unwrap any 3D model so textures apply correctly. Mark seams, unwrap, and pack UVs for the best texture resolution.",
            "uv_unwrapping"
        ));
        allTutorials.add(new Tutorial(
            "✨", "Procedural Textures",
            "Materials & Textures", "Advanced", "50 min",
            "Build complex materials entirely with nodes — no image textures needed. Create wood, marble, rust, and fabric procedurally.",
            "procedural_textures"
        ));
    }

    /*
     * Returns all tutorials.
     * Used by the See All screen and the initial full list.
     */
    public List<Tutorial> getAll() {
        return new ArrayList<>(allTutorials);
    }

    /*
     * Returns tutorials filtered by category name.
     * Category must match exactly what is stored in Tutorial.category.
     */
    public List<Tutorial> getByCategory(String category) {
        List<Tutorial> result = new ArrayList<>();
        for (Tutorial t : allTutorials) {
            if (t.getCategory().equals(category)) {
                result.add(t);
            }
        }
        return result;
    }

    /*
     * Returns tutorials whose title or category contains the query.
     * Case-insensitive. Used by the search bar.
     */
    public List<Tutorial> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        String lower = query.toLowerCase().trim();
        List<Tutorial> result = new ArrayList<>();
        for (Tutorial t : allTutorials) {
            if (t.getTitle().toLowerCase().contains(lower)
                || t.getCategory().toLowerCase().contains(lower)
                || t.getDifficulty().toLowerCase().contains(lower)
                || t.getDescription().toLowerCase().contains(lower)) {
                result.add(t);
            }
        }
        return result;
    }

    /*
     * Returns the beginner tutorial used as the default
     * when a user has never started anything.
     */
    public Tutorial getBeginnerDefault() {
        return allTutorials.get(0); // "Welcome to Blender"
    }

    /*
     * Finds a tutorial by its tutorialId.
     * Used to reload the last-viewed tutorial for Continue Learning.
     */
    public Tutorial findById(String tutorialId) {
        for (Tutorial t : allTutorials) {
            if (t.getTutorialId().equals(tutorialId)) {
                return t;
            }
        }
        return null;
    }
}
