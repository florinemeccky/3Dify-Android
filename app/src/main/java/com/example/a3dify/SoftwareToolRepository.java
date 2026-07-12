package com.example.a3dify;

import android.graphics.Color;
import com.example.a3dify.models.SoftwareTool;
import java.util.ArrayList;
import java.util.List;

/*
 * SoftwareToolRepository
 * Single source of truth for all 3D software entries.
 * Static data — no network or database required.
 */
public class SoftwareToolRepository {

    private static SoftwareToolRepository instance;
    private final List<SoftwareTool> tools = new ArrayList<>();

    public static SoftwareToolRepository getInstance() {
        if (instance == null) instance = new SoftwareToolRepository();
        return instance;
    }

    private SoftwareToolRepository() {
        load();
    }

    private void load() {

        tools.add(new SoftwareTool(
            "Blender",
            "The industry-standard free 3D suite",
            "Free",
            "blender.org",
            "Blender is a free and open-source 3D creation suite that supports the entire 3D pipeline — modeling, rigging, animation, simulation, rendering, compositing, and motion tracking. It is the most beginner-friendly professional 3D tool available and has a massive community.",
            "OS: Windows 8.1 / macOS 10.13 / Linux\nCPU: 64-bit quad-core, 2 GHz\nRAM: 8 GB\nGPU: 2 GB VRAM, OpenGL 4.3\nStorage: 1 GB",
            "OS: Windows 10 / macOS 11 / Linux\nCPU: 64-bit eight-core\nRAM: 32 GB\nGPU: 8 GB VRAM\nStorage: SSD recommended",
            "Excellent",
            "Modeling, Animation, VFX, Game Assets, Motion Graphics, Architectural Visualization",
            "G — Grab/Move\nR — Rotate\nS — Scale\nE — Extrude\nCtrl+R — Loop Cut\nTab — Toggle Edit Mode\nNumpad 5 — Toggle Perspective\nShift+A — Add Object",
            "Start with the official Blender fundamentals playlist on YouTube. Do not skip the interface tour. Learn the numpad for viewport navigation early. Join the Blender Artists community forum for feedback on your work.",
            Color.parseColor("#FF6A00")
        ));

        tools.add(new SoftwareTool(
            "Autodesk Maya",
            "Industry standard for film and VFX animation",
            "Paid (Student Free)",
            "autodesk.com/maya",
            "Maya is the industry standard for character animation, VFX, and film production. Used by studios like Pixar, ILM, and Weta Digital. It has an extremely powerful rigging and animation system but a steep learning curve for beginners.",
            "OS: Windows 10 / macOS 10.15 / Linux\nCPU: 64-bit Intel or AMD multi-core\nRAM: 8 GB\nGPU: 1 GB VRAM, Storage: 4 GB",
            "OS: Windows 10 or 11\nCPU: 64-bit Intel or AMD, 3.0 GHz+\nRAM: 16–32 GB\nGPU: 4 GB VRAM, certified GPU\nStorage: SSD",
            "Moderate",
            "Character Animation, VFX, Film Production, Game Rigging, Fluid Simulations",
            "W — Move\nE — Rotate\nR — Scale\nQ — Select Tool\nCtrl+Z — Undo\nF — Frame Selection\nSpace — Hotbox\n4/5/6 — Wireframe/Smooth/Texture",
            "Students get free access through Autodesk Education. Focus on the Graph Editor early as it is central to all animation. Maya's MEL and Python scripting are powerful skills worth learning. Use it alongside Blender to understand different workflows.",
            Color.parseColor("#4A90E2")
        ));

        tools.add(new SoftwareTool(
            "3ds Max",
            "Architectural visualization and game environments",
            "Paid (Student Free)",
            "autodesk.com/3dsmax",
            "3ds Max is widely used in architectural visualization, game environment design, and product visualization. It has a long history in the Windows ecosystem and integrates well with game engines like Unreal Engine and Unity.",
            "OS: Windows 10 or 11 (64-bit only)\nCPU: 64-bit Intel or AMD, 3.0 GHz\nRAM: 8 GB\nGPU: 1 GB VRAM, DirectX 11\nStorage: 6 GB",
            "OS: Windows 10 or 11\nCPU: 64-bit, 3.5 GHz+\nRAM: 16–32 GB\nGPU: 4 GB VRAM\nStorage: SSD, 10 GB+",
            "Moderate",
            "Architectural Visualization, Game Environments, Product Rendering, Motion Graphics",
            "W — Move\nE — Rotate\nR — Scale\nM — Material Editor\nH — Select by Name\nAlt+W — Maximize Viewport\nF4 — Edge Faces View",
            "3ds Max is Windows-only. Learn the modifier stack early as it is central to the 3ds Max workflow. The MAXScript language is useful for automating repetitive tasks. It pairs very well with V-Ray and Corona renderer for architectural work.",
            Color.parseColor("#2ECC71")
        ));

        tools.add(new SoftwareTool(
            "Cinema 4D",
            "Motion graphics and broadcast design",
            "Paid",
            "maxon.net/cinema-4d",
            "Cinema 4D is renowned for its ease of use and excellent motion graphics capabilities through MoGraph. It is the tool of choice for broadcast design and integrates seamlessly with Adobe After Effects. It is more accessible than Maya but still professional grade.",
            "OS: Windows 10 / macOS 10.15\nCPU: 64-bit multi-core\nRAM: 8 GB\nGPU: 1 GB VRAM, Storage: 10 GB",
            "OS: Windows 10+ / macOS 11+\nCPU: 64-bit multi-core, 3.0 GHz+\nRAM: 16–32 GB\nGPU: 4 GB VRAM\nStorage: SSD",
            "Good",
            "Motion Graphics, Broadcast Design, Product Visualization, After Effects Integration",
            "E — Move\nR — Rotate\nT — Scale\nC — Make Editable\nN — Normals Display\nCtrl+Z — Undo\nF1-F4 — Viewport Views",
            "If you use Adobe After Effects, Cinema 4D is a natural companion. The MoGraph system makes complex motion graphics achievable with little code. Maxon offers a free trial and educational pricing. Start with the MoGraph basics to see what makes C4D unique.",
            Color.parseColor("#9B59B6")
        ));

        tools.add(new SoftwareTool(
            "ZBrush",
            "Digital sculpting for characters and creatures",
            "Paid",
            "maxon.net/zbrush",
            "ZBrush is the industry leader for digital sculpting, used to create characters, creatures, and highly detailed organic models for film, games, and 3D printing. It handles extremely high polygon counts that other software cannot. The interface is unconventional but extremely powerful.",
            "OS: Windows 7+ / macOS 10.12+\nCPU: Any Intel or AMD 64-bit\nRAM: 8 GB\nGPU: Not GPU dependent\nStorage: 8 GB",
            "OS: Windows 10 / macOS 11\nCPU: Intel i7 / AMD Ryzen 7+\nRAM: 32–64 GB\nGPU: Not critical for ZBrush\nStorage: SSD",
            "Difficult",
            "Character Sculpting, Creature Design, Game Assets, 3D Printing, Concept Art",
            "B — Brush Menu\nS — Brush Size\nI — Brush Intensity\nCtrl — Smooth\nAlt+Click — Invert Brush\nCtrl+Z — Undo\nT — Edit Mode\nX — Mirror Sculpting",
            "ZBrush has a very unusual interface — do not give up in the first week. Learn to sculpt in multiple subdivision levels. Always start with basic forms before adding detail. ZBrush pairs excellently with Blender for retopology and final rendering.",
            Color.parseColor("#E74C3C")
        ));
    }

    public List<SoftwareTool> getAll() {
        return new ArrayList<>(tools);
    }

    public SoftwareTool findByName(String name) {
        for (SoftwareTool t : tools) {
            if (t.getName().equals(name)) return t;
        }
        return null;
    }
}