package org.spirit.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.NONE)
public class SpiritCompileMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		System.out.println("hello world!");
	}
}
