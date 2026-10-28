package com.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.shared.Tooltip.AriaLinkMode;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.sidenav.SideNavVariant;
import com.vaadin.flow.router.Layout;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createApplicationHeader(), createApplicationDrawer(), createRailNavToggle());
        UI.getCurrentOrThrow().addClassName("aura-accent-neutral");
    }

    private Component createRailNavToggle() {
        var toggle = new Button(LucideIcon.PANEL_LEFT.create());
        toggle.addThemeVariants(ButtonVariant.TERTIARY);
        toggle.addClassName("rail-nav-toggle");
        toggle.setAriaLabel("Toggle sidebar");
        Tooltip.forComponent(toggle).withText("Toggle sidebar").withAriaLinkMode(AriaLinkMode.NONE);
        toggle.addClickListener(e -> {
            if (this.getClassNames().contains("nav-rail")) {
                this.removeClassName("nav-rail");
            } else {
                this.addClassName("nav-rail");
            }
        });
        return toggle;
    }

    private Component createApplicationHeader() {
        var appLogo = new Avatar("My App");
        appLogo.setAbbreviation("M");
        appLogo.addClassName("app-logo");
        appLogo.addThemeVariants(AvatarVariant.AURA_FILLED, AvatarVariant.XSMALL);

        var appName = new Span("My App");
        appName.addClassName("app-name");

        var header = new HorizontalLayout(appLogo, appName);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(true);
        header.addClassName("app-header");
        return header;
    }

    private Component createApplicationDrawer() {
        var scroller = new Scroller(createPrimaryNavigation());
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    private SideNav createPrimaryNavigation() {
        var nav = new SideNav();
        nav.addThemeVariants(SideNavVariant.AURA_FILLED);
        nav.addItem(
                item("Home", "", LucideIcon.HOUSE),
                item("People", "people", LucideIcon.USERS),
                item("Payroll", "payroll", LucideIcon.SQUARE_PERCENT),
                item("Finance", "finance", LucideIcon.BANKNOTE),
                item("Engage", "engage", LucideIcon.TRENDING_UP),
                item("IT", "it", LucideIcon.LAPTOP),
                item("Reports", "reports", LucideIcon.CHART_NO_AXES_COLUMN),
                item("Automations", "automations", LucideIcon.ZAP));
        return nav;
    }

    private SideNavItem item(String label, String path, LucideIcon icon) {
        var item = new SideNavItem(label, path, icon.create());
        item.setMatchNested(true);
        return item;
    }
}
