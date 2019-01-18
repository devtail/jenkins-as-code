#!groovy

/*
 * This script configures the simple theme plugin.
 * Requires the simple theme plugin to be installed.
 * Tested with simple-theme-plugin:0.5.1
 *
 * Use http://afonsof.com/jenkins-material-theme/ to generate a new jenkins theme.
 * Place the theme at the userContent directory of Jenkins to be publicly available
 */

import jenkins.model.Jenkins
import org.jenkinsci.plugins.simpletheme.CssUrlThemeElement


def themeDecorator = Jenkins.instance.getExtensionList(org.codefirst.SimpleThemeDecorator.class).first()

themeDecorator.setElements([
  new CssUrlThemeElement('https://fishi.devtail.io/content-images/jenkins-devtail-theme.css')
])

Jenkins.instance.save()

