package org.spirit.maven.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.NONE, //
		requiresDependencyResolution = ResolutionScope.COMPILE)
public class SpiritCompileMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}")
	public MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		System.out.println("hello world!");
		try {
			ClassLoader loader = getClassLoader(project);
			Class<?> clazz = loader.loadClass("com.sum.spirit.example.ClassGenericTest");
			System.out.println(clazz.getName());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public ClassLoader getClassLoader(MavenProject project) {
		try {
			// 所有的类路径
			List<String> classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			// 转为URL数组
			URL urls[] = new URL[classpathElements.size()];
			for (int index = 0; index < classpathElements.size(); index++) {
				urls[index] = new File((String) classpathElements.get(index)).toURL();
			}
			// 自定义类加载器
			return new URLClassLoader(urls, this.getClass().getClassLoader());

		} catch (Exception e) {
			getLog().debug("Couldn't get the classloader.");
			return this.getClass().getClassLoader();
		}
	}
}
