package com.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.sidenav.SideNavVariant;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createApplicationHeader(), createApplicationDrawer());
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
        return header;
    }

    private Component createApplicationDrawer() {
        var scroller = new Scroller(createPrimaryNavigation());
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    private SideNav createPrimaryNavigation() {
        var nav = new SideNav();
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
