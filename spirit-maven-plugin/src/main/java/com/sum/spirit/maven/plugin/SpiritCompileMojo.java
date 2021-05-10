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
import com.sum.spirit.starter.JavaStarter;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.NONE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class SpiritCompileMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}")
	public MavenProject project;
	@Parameter
	private String inputPath;
	@Parameter
	private String outputPath;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			if (inputPath == null) {
				inputPath = project.getResources().get(0).getDirectory();
				inputPath = inputPath.endsWith("\\resources") ? inputPath + "\\sources" : inputPath + "/sources";
			}
			if (outputPath == null) {
				outputPath = project.getBuild().getSourceDirectory();
			}
			System.out.println(inputPath);
			System.out.println(outputPath);

			List<String> classpaths = project.getCompileClasspathElements();
			classpaths.add(project.getBuild().getTestOutputDirectory());
			System.out.println(classpaths);

			JavaStarter.main(new String[] { "--input=" + inputPath, "--output=" + outputPath, "--classpaths=" + Joiner.on(", ").join(classpaths) });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		String inputPath = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\src\\main\\resources\\sources";
		String outputPath = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\src\\main\\java";
		String classpaths = "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\target\\classes, ";
		classpaths += "C:\\Users\\tao.chen1\\.m2\\repository\\com\\sum\\spirit\\spirit-example-common\\2.1.30\\spirit-example-common-2.1.30.jar, ";
		classpaths += "D:\\Work\\CloudSpace\\spirit\\spirit-example\\spirit-example-plugin\\target\\test-classes";
		JavaStarter.main(new String[] { "--input=" + inputPath, "--output=" + outputPath, "--classpaths=" + classpaths });
	}
}
