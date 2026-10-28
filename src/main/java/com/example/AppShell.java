package com.example;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;

@StyleSheet(value = Aura.STYLESHEET, layer = "aura")
@StyleSheet("styles.css") // Your custom styles
@Push
public class AppShell implements AppShellConfigurator {
}
