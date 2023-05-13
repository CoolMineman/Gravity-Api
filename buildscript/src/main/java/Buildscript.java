import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.coolcrabs.brachyura.fabric.FabricContext;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.SimpleFabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.quilt.QuiltMappings;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleFabricProject {
    @Override
    public String getMavenGroup() {
        return "com.fusionflux";
    }

    @Override
    public VersionMeta createMcVersion() {
        return Minecraft.getVersion("1.19.2");
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }

    @Override
    public MappingTree createMappings() {
        return QuiltMappings.ofMaven(QuiltMaven.URL, QuiltMaven.quiltMappings("1.19.2+build.22")).toIntermediary(context.get().intermediary.get());
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.14.17"));
    }

    @Override
    protected FabricContext createContext() {
        return new SimpleFabricContext(){
            @Override
            public List<Path> getCompileDependencies() {
                List<Path> paths = super.getCompileDependencies();
                ArrayList<Path> a = new ArrayList<>();
                ArrayList<Path> b = new ArrayList<>();
                for (Path p : paths) {
                    if (p.getFileName().toString().contains("MixinExtras")) {
                        a.add(p);
                    } else {
                        b.add(p);
                    }
                }
                a.addAll(b);
                return a;
            }
        };
    }


    @Override
    public void getModDependencies(ModDependencyCollector d) {
        // Libraries
        String[][] fapiModules = new String[][] {
            {"fabric-registry-sync-v0", "0.9.14+92cf9a3ecd"},
            {"fabric-resource-loader-v0", "0.5.2+9e7660c6cd"},
            {"fabric-renderer-api-v1", "1.0.7+9ff28f40cd"},
            {"fabric-item-groups-v0", "0.3.22+9ff28f40cd"},
            {"fabric-object-builder-api-v1", "4.0.4+9ff28f40cd"},
            {"fabric-rendering-v1", "1.10.13+9ff28f40cd"},
            {"fabric-networking-api-v1", "1.0.25+9ff28f40cd"},
            {"fabric-api-base", "0.4.8+e62f51a3cd"},
            {"fabric-models-v0", "0.3.14+9ff28f40cd"},
            {"fabric-renderer-indigo", "0.6.5+9ff28f40cd"},
            {"fabric-entity-events-v1", "1.4.15+9ff28f40cd"},
            {"fabric-events-interaction-v0", "0.4.25+9ff28f40cd"},
            {"fabric-rendering-data-attachment-v1", "0.3.11+9ff28f40cd"},
            {"fabric-command-api-v2", "2.2.1+413cbbc790"},
            {"fabric-mining-level-api-v1", "2.1.6+9ff28f40cd"}
        };
        for (String[] module : fapiModules) {
            d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        }
        jij(d.addMaven("https://jitpack.io", new MavenId("com.github.LlamaLad7:MixinExtras:0.1.1"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
        d.addMaven("https://maven.terraformersmc.com/", new MavenId("com.terraformersmc:modmenu:4.0.6"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        jij(d.addMaven("https://ladysnake.jfrog.io/artifactory/mods", new MavenId("dev.onyxstudios.cardinal-components-api:cardinal-components-base:5.0.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
        jij(d.addMaven("https://ladysnake.jfrog.io/artifactory/mods", new MavenId("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:5.0.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
        jij(d.addMaven("https://ladysnake.jfrog.io/artifactory/mods", new MavenId("dev.onyxstudios.cardinal-components-api:cardinal-components-world:5.0.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
    }
}
