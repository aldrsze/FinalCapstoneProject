package com.inventorysystem.gui;

import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Centralized UI constants for the Inventory Management System.
 * Contains definitions for Colors, Fonts, Dimensions, and Borders to ensure
 * visual consistency across the application.
 */
public class UIConstants {

    // ===================================================================================
    //  COLOR PALETTE
    // ===================================================================================

    // --- Base Neutrals ---
    public static final Color WHITE               = Color.WHITE;
    public static final Color GREY_COLOR          = new Color(149, 165, 166);

    // --- Brand & Primary Colors ---
    public static final Color PRIMARY_COLOR       = new Color(41, 128, 185);
    public static final Color PRIMARY_DARK        = new Color(31, 97, 141);
    public static final Color PRIMARY_LIGHT       = new Color(52, 152, 219);

    // --- Accent Colors ---
    public static final Color ACCENT_COLOR        = new Color(52, 152, 219);
    public static final Color ACCENT_DARK         = new Color(41, 128, 185);
    public static final Color TEAL_COLOR          = new Color(22, 160, 133); // Dashboard Titles

    // ===================================================================================
    //  FUNCTIONAL / STATUS COLORS
    // ===================================================================================

    // --- Success ---
    public static final Color SUCCESS_COLOR       = PRIMARY_COLOR;           // Default theme success
    public static final Color SUCCESS_GREEN       = new Color(46, 204, 113); // Explicit Green
    public static final Color SUCCESS_GREEN_HOVER = new Color(39, 174, 96);
    public static final Color SUCCESS_DARK        = new Color(39, 174, 96);

    // --- Danger / Error ---
    public static final Color DANGER_COLOR        = new Color(231, 76, 60);  // Standard Red
    public static final Color DANGER_DARK         = new Color(192, 57, 43);
    public static final Color DANGER_CRITICAL     = new Color(244, 67, 54);  // Bright Red

    // --- Warning ---
    public static final Color WARNING_COLOR       = new Color(243, 156, 18); // Yellow/Orange
    public static final Color WARNING_DARK        = new Color(211, 84, 0);
    public static final Color WARNING_DEEP        = new Color(255, 87, 34);  // Deep Orange

    // --- Info & Other Statuses ---
    public static final Color INFO_BLUE           = new Color(33, 150, 243); // Info/Overstocked
    public static final Color PURPLE_COLOR        = new Color(156, 39, 176); // Refunds
    public static final Color BROWN_COLOR         = new Color(121, 85, 72);  // Disposals
    public static final Color EXPORT_BTN_COLOR    = new Color(40, 167, 69);  // Excel Green

    // ===================================================================================
    //  COMPONENT COLORS
    // ===================================================================================

    // --- Backgrounds ---
    public static final Color BACKGROUND_COLOR    = new Color(245, 248, 250); // App Main BG
    public static final Color FORM_COLOR          = Color.WHITE;              // Panels/Forms
    public static final Color OVERLAY_BACKGROUND  = new Color(255, 255, 255, 240); // Login Overlay

    // --- Navigation ---
    public static final Color NAV_BACKGROUND      = new Color(52, 73, 94);
    public static final Color NAV_HOVER           = new Color(69, 90, 100);

    // --- Typography Colors ---
    public static final Color TEXT_PRIMARY        = new Color(52, 73, 94);
    public static final Color TEXT_DARK           = new Color(44, 62, 80);    // Headings
    public static final Color TEXT_SECONDARY      = new Color(149, 165, 166);
    public static final Color TEXT_SUBTITLE       = new Color(127, 140, 141);
    public static final Color TEXT_LIGHT          = new Color(189, 195, 199);

    // --- Borders ---
    public static final Color BORDER_COLOR        = new Color(206, 214, 224);
    public static final Color BORDER_DARK         = new Color(189, 195, 199);

    // ===================================================================================
    //  TYPOGRAPHY
    // ===================================================================================

    // --- Brand & Headers ---
    public static final Font FONT_BRAND           = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font TITLE_FONT           = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT        = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_WELCOME         = new Font("Segoe UI", Font.BOLD, 20);

    // --- Standard UI Elements ---
    public static final Font LABEL_FONT           = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LABEL_BOLD_FONT      = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font INPUT_FONT           = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT          = new Font("Segoe UI", Font.BOLD, 13);

    // --- Table Fonts ---
    public static final Font TABLE_HEADER_FONT    = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font TABLE_FONT           = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TABLE_SMALL_FONT     = new Font("Segoe UI", Font.PLAIN, 10);

    // --- Specific Size Variants ---
    public static final Font LABEL_FONT_PLAIN_12  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font LABEL_FONT_PLAIN_13  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font LABEL_FONT_PLAIN_15  = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font LABEL_FONT_BOLD_16   = new Font("Segoe UI", Font.BOLD, 16);

    // --- Icons & Special Fonts ---
    public static final Font FONT_ICON_LARGE      = new Font("Segoe UI Emoji", Font.BOLD, 55);
    public static final Font FONT_EMOJI_LARGE     = new Font("Segoe UI Emoji", Font.PLAIN, 24);
    public static final Font FONT_EMOJI_SMALL     = new Font("Segoe UI Emoji", Font.PLAIN, 16);
    public static final Font FONT_MONOSPACE       = new Font("Monospaced", Font.PLAIN, 12);

    // ===================================================================================
    //  DIMENSIONS & SIZES
    // ===================================================================================

    // --- Panel Layout ---
    public static final int NAV_PANEL_WIDTH       = 280;
    public static final int SIDE_PANEL_WIDTH      = 320;

    // --- Component Sizes ---
    public static final Dimension BUTTON_DIMENSION       = new Dimension(140, 38);
    public static final Dimension LARGE_BUTTON_DIMENSION = new Dimension(180, 42);
    public static final int INPUT_HEIGHT                 = 38; // Adjusted from 30 for better touch/click targets

    // --- Table Dimensions ---
    public static final int TABLE_ROW_HEIGHT      = 35;
    public static final int TABLE_HEADER_HEIGHT   = 40;

    // --- Dialog Sizes ---
    public static final Dimension DIALOG_LARGE         = new Dimension(950, 650);
    public static final Dimension DIALOG_MEDIUM        = new Dimension(550, 550);
    public static final Dimension DIALOG_SMALL         = new Dimension(400, 300);
    public static final Dimension DIALOG_LOGIN_WELCOME = new Dimension(380, 220);

    // --- Spacing & Padding ---
    public static final int PANEL_PADDING         = 20;
    public static final int COMPONENT_SPACING     = 10;
    public static final int SECTION_SPACING       = 15;

    // ===================================================================================
    //  STYLES & BORDERS
    // ===================================================================================

    public static final Border BORDER_THIN        = new LineBorder(BORDER_COLOR, 1);
    public static final Border BORDER_INPUT       = new LineBorder(BORDER_DARK, 1, true);

    // Private constructor to prevent instantiation
    private UIConstants() {}
}