package org.hibernate.eclipse.launch;

import org.eclipse.osgi.util.NLS;

public class LaunchMessages extends NLS {
	public static final String BUNDLE_NAME = "org.hibernate.eclipse.launch.LaunchMessages"; //$NON-NLS-1$

	public static String ExporterFactory_output_dir_in_does_not_exist;
	public static String ExporterFactory_template_dir_in_does_not_exist;
	public static String ExporterProperty_generate_ejb3_annotations;
	public static String ExporterProperty_use_java5_syntax;
	public static String LaunchHelper_launch_cfg_type_cannot_be_null;
	public static String ConsoleConfigurationWizardPage_config_name_already_exist;
	public static String ConsoleConfigurationWizardPage_name_must_specified;
	public static String ConsoleConfigurationWizardPage_bad_name;
	public static String ConsoleConfigurationWizardPage_bad_char;
	public static String CodeGenerationSettingsTab_script_can_not_be_generated_correctly_without;
	public static String ConsoleConfigurationLaunchDelegate_direct_launch_not_supported;
	public static String ExporterAttributes_could_not_locate_exporter_for_in;
	public static String PathHelper_does_not_exist;
	public static String PathHelper_has_invalid_variable_references;
	public static String PathHelper_not_directory;
	public static String PathHelper_not_file;
	public static String PathHelper_has_to_be_folder_or_project;
	public static String PathHelper_has_to_be_file;
	public static String PathHelper_project_for_is_closed;
	public static String HibernateRefactoringUtil_error_during_refactoring;
	public static String HibernateRefactoringUtil_error_occured_while_updating_classpath;
	public static String LaunchConfigurationResourceNameChange_update_resource_path_in_launch_cfg;
	public static String MoveResourceParticipant_launch_configurations_updates;
	public static String RenameResourceParticipant_launch_configurations_updates;
	public static String ConsoleConfigurationITypeRenameParticipant_update;
	public static String ConsoleConfigurationITypeRenameParticipant_update_names;
	public static String ConnectionProfileRenameChange_update_connection_profile_name;
	public static String ConnectionProfileRenameParticipant_launch_configurations_updates;
	public static String DeleteProjectParticipant_console_configurations_updates;
	public static String DeleteProjectParticipant_delete_console_configuration;

	static {
		NLS.initializeMessages(BUNDLE_NAME, LaunchMessages.class);
	}

	private LaunchMessages() {
	}
}
