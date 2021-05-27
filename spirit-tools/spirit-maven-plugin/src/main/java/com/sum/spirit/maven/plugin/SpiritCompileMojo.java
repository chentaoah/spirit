package com.sum.spirit.maven.plugin;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Joiner;
import com.sum.spirit.starter.java.JavaStarter;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.NONE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class SpiritCompileMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}")
	public MavenProject project;
	@Parameter
	private String inputPath;
	@Parameter
	private String outputPath;
	@Parameter
	private String langPackage;
	@Parameter
	private String utilPackage;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			if (inputPath == null) {
				inputPath = project.getResources().get(0).getDirectory();
				inputPath = inputPath.endsWith("\\resources") ? inputPath + "\\sources" : inputPath + "/sources";
			}
			outputPath = outputPath == null ? project.getBuild().getSourceDirectory() : outputPath;

			getLog().info("");
			getLog().info("-----------------------[ inputPath, outputPath ]------------------------");
			getLog().info(inputPath);
			getLog().info(outputPath);
			getLog().info("");

			getLog().info("-----------------------------[ classpaths ]-----------------------------");
			List<String> classpaths = project.getCompileClasspathElements();
			classpaths.add(project.getBuild().getTestOutputDirectory());
			classpaths.forEach(getLog()::info);
			getLog().info("");

			langPackage = langPackage == null ? "java.lang" : langPackage;
			utilPackage = utilPackage == null ? "java.util" : utilPackage;

			JavaStarter.main(new String[] { //
					"--input=" + inputPath, //
					"--output=" + outputPath, //
					"--classpaths=" + Joiner.on(", ").join(classpaths), //
					"--langPackage=" + langPackage, //
					"--utilPackage=" + utilPackage //
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		String userHome = System.getProperty("user.home");
		String inputPath = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\src\\main\\resources\\sources";
		String outputPath = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\src\\main\\java";
		String classpaths = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\target\\classes, ";
		classpaths += userHome + "\\.m2\\repository\\com\\gitee\\chentaoah\\spirit-example-common\\2.1.30\\spirit-example-common-2.1.30.jar, ";
		classpaths += userHome + "\\.m2\\repository\\com\\gitee\\chentaoah\\spirit-stdlib\\2.1.30\\spirit-stdlib-2.1.30.jar, ";
		classpaths += userHome + "\\.m2\\repository\\org\\slf4j\\slf4j-api\\1.7.25\\slf4j-api-1.7.25.jar, ";
		classpaths += userHome + "\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.9\\commons-lang3-3.9.jar, ";
		classpaths += "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\target\\test-classes";
		JavaStarter.main(new String[] { //
				"--input=" + inputPath, //
				"--output=" + outputPath, //
				"--classpaths=" + classpaths, //
				"--langPackage=" + "java.lang", //
				"--utilPackage=" + "java.util" //
		});
	}
}
