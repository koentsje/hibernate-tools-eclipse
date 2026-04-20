package org.hibernate.eclipse.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.IInternalDebugUIConstants;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.eclipse.console.HibernateBasePlugin;
import org.hibernate.eclipse.console.utils.DialogSelectionHelper;
import org.hibernate.tool.eclipse.common.base.core.utils.DriverClassHelpers;
import org.xml.sax.EntityResolver;

public class ConsoleConfigurationOptionsTab extends ConsoleConfigurationTab {

	private Combo dialectNameCombo;
	private Text entityResolverClassNameText;
	private Text namingStrategyClassNameText;
	private DriverClassHelpers helper = new DriverClassHelpers();

	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		comp.setLayout(layout);
		comp.setFont(font);

		createDialectNameCombo( comp );
		createNamingStrategyClassNameEditor( comp );
		createEntityResolverClassNameEditor( comp );

	}
	
	private void createDialectNameCombo(Composite parent) {
		Group group = createGroup( parent, BasicHibernateMessages.NewConfigurationWizardPage_database_dialect );
		
		dialectNameCombo = new Combo(group, SWT.NONE);
		dialectNameCombo.setItems(helper.getDialectNames());
		Font font=parent.getFont();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		dialectNameCombo.setLayoutData(gd);
		dialectNameCombo.setFont(font);
		dialectNameCombo.addModifyListener(getChangeListener());
	}

	private void createNamingStrategyClassNameEditor(Composite parent) {
		Group group = createGroup( parent, BasicHibernateMessages.ConsoleConfigurationOptionsTab_naming_strategy );
		namingStrategyClassNameText = createBrowseEditor( parent, group);
		createBrowseButton( group, new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleNamingStrategyBrowse();
			}
		});
	}

	private void createEntityResolverClassNameEditor(Composite parent) {
		Group group = createGroup( parent, BasicHibernateMessages.ConsoleConfigurationOptionsTab_entity_resolver );
		entityResolverClassNameText = createBrowseEditor( parent, group);
		createBrowseButton( group, new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleEntityResolverBrowse();
			}
		});
	}


	public String getName() {
		return BasicHibernateMessages.ConsoleConfigurationOptionsTab_options;
	}

	public Image getImage() {
		return DebugUITools.getImage(IInternalDebugUIConstants.IMG_OBJS_COMMON_TAB);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String dialect = configuration.getAttribute( IBasicHibernateLaunchConstants.DIALECT, "" ); //$NON-NLS-1$
			dialectNameCombo.setText( helper.getShortDialectName(dialect) );
			namingStrategyClassNameText.setText( configuration.getAttribute( IBasicHibernateLaunchConstants.NAMING_STRATEGY, "" ) ); //$NON-NLS-1$
			entityResolverClassNameText.setText( configuration.getAttribute( IBasicHibernateLaunchConstants.ENTITY_RESOLVER, "" ) ); //$NON-NLS-1$
		}
		catch (CoreException e) {
			HibernateBasePlugin.getDefault().log(e);
		}
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String dialect = nonEmptyTrimOrNull( dialectNameCombo.getText() );
		dialect = helper.getDialectClass(dialect);
		
		configuration.setAttribute( IBasicHibernateLaunchConstants.DIALECT, nonEmptyTrimOrNull( dialect ) );
		configuration.setAttribute( IBasicHibernateLaunchConstants.NAMING_STRATEGY, nonEmptyTrimOrNull( namingStrategyClassNameText ) );
		configuration.setAttribute( IBasicHibernateLaunchConstants.ENTITY_RESOLVER, nonEmptyTrimOrNull( entityResolverClassNameText ) );
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {

	}


	private void handleEntityResolverBrowse() {
		String string = DialogSelectionHelper.chooseImplementation(EntityResolver.class.getName(), entityResolverClassNameText.getText(), BasicHibernateMessages.ConsoleConfigurationOptionsTab_select_entity_resolver_class, getShell());
		if(string!=null) {
			entityResolverClassNameText.setText(string);
		}
	}

	private void handleNamingStrategyBrowse() {
		String string = DialogSelectionHelper.chooseImplementation("org.hibernate.cfg.NamingStrategy", namingStrategyClassNameText.getText(), BasicHibernateMessages.ConsoleConfigurationOptionsTab_select_naming_strategy_class, getShell());
		if(string!=null) {
			namingStrategyClassNameText.setText(string);
		}
	}

}
