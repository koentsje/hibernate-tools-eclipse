package org.hibernate.tool.eclipse.orm.console.core.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.orm.console.core.nls.Messages"; //$NON-NLS-1$
	public static String ConsoleConfiguration_connection_profile_not_found;
	public static String ConsoleConfiguration_could_not_access;
	public static String ConsoleConfiguration_could_not_configure_entity_resolver;
	public static String ConsoleConfiguration_could_not_configure_naming_strategy;
	public static String ConsoleConfiguration_could_not_create_jpa_based_configuration;
	public static String ConsoleConfiguration_could_not_load_annotationconfiguration;
	public static String ConsoleConfiguration_could_not_load_jpa_configuration;
	public static String ConsoleConfiguration_could_not_parse_configuration;
	public static String ConsoleConfiguration_factory_not_closed_before_build_new_factory;
	public static String ConsoleConfiguration_invalid_configuration;
	public static String ConsoleConfiguration_persistence_unit_not_found;
	public static String ConsoleConfiguration_problems_while_loading_database_driverclass;
	public static String ConsoleConfiguration_null_execution_context;
	public static String JavaPage_no_info;
	public static String JavaPage_not_allowed;
	public static String KnownConfigurations_could_not_write_state;
	public static String KnownConfigurations_hibernate_log;
	public static String KnownConfigurations_unknown;
	public static String DefaultExecutionContext_existing_classloader;
	public static String ClassNode_uninitialized_proxy;
	public static String ConfigurationNode_mapped_entities;
	public static String NodeFactory_unknown;
	public static String PersistentCollectionNode_could_not_access_property_value;
	public static String AbstractConsoleConfigurationPreferences_could_not_load_prop_file;
	public static String AbstractConsoleConfigurationPreferences_name_not_null_or_empty;
	public static String AbstractConsoleConfigurationPreferences_unknown;
	public static String StandAloneConsoleConfigurationPreferences_could_not_resolve_classpaths;
	public static String StandAloneConsoleConfigurationPreferences_errors_while_parsing;
	public static String StringListDialog_Add;
	public static String StringListDialog_Add_Element;
	public static String StringListDialog_Elements;
	public static String StringListDialog_Modify;
	public static String StringListDialog_Modify_Element;
	public static String StringListDialog_New_Element;
	public static String StringListDialog_Remove;
public static String DynamicSQLPreviewView_caused_by;
	public static String DynamicSQLPreviewView_manipulation_of;
	public static String EclipseConsoleConfigurationPreferences_could_not_compute_def_classpath;
	public static String EclipseConsoleConfigurationPreferences_could_not_resolve_to_file;
	public static String EclipseConsoleConfigurationPreferences_could_not_resolve_classpaths;
	public static String EclipseConsoleConfigurationPreferences_errors_while_parsing;
	public static String HibernateNature_error_while_performing_background_reading_of_database_schema;
	public static String HibernateNature_reading_database_metadata;
	public static String HibernateNature_reading_database_metadata_for;
	public static String AbstractQueryEditor_do_you_want_open_session_factory;
	public static String AbstractQueryEditor_open_session_factory;
	public static String HibernateBasePlugin_no_message_1;
	public static String HibernateBasePlugin_no_message_2;
	public static String ProjectUtils_could_not_save_changes_to_preferences;
	public static String ProjectUtils_could_not_activate_hibernate_nature_on_project;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
