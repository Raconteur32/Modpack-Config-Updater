package fr.raconteur.sbcou.screen.editor;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.raconteur.sbcou.OptionChange;
import fr.raconteur.sbcou.OptionHandlingState;
import fr.raconteur.sbcou.OptionsActionsContext;
import fr.raconteur.sbcou.OptionsActionsUtils;
import fr.raconteur.sbcou.exceptions.OptionsActionsContextException;
import fr.raconteur.sbcou.exceptions.OptionsActionsUtilsException;
import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.flatobject.FlatKey;
import fr.raconteur.sbcou.flatobject.FlatKeyValuePair;
import fr.raconteur.sbcou.screen.newversion.NewVersionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.layouts.FrameLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SbcouScreen extends Screen {
  private static final int CONTENT_Y = 32;
  private static final int FOOTER_HEIGHT = 32;
  private static int CONTENT_HEIGHT;
  private FlatKey currentKey;
  private EntryList entryList;
  private final Screen parentScreen;
  int screenBorderWidth;
  int layoutWidth;
  List<AbstractWidget> headingWidgets = new ArrayList<>();

  public SbcouScreen(Screen parentScreen) {
    super(Component.literal("Sbcou Screen"));
    this.parentScreen = parentScreen;

    currentKey = FlatKey.EMPTY;
  }

  @Override
  protected void init() {
    CONTENT_HEIGHT = this.height - CONTENT_Y - FOOTER_HEIGHT;
    screenBorderWidth = this.width / 20;
    layoutWidth = this.width - (2 * screenBorderWidth);

    refresh(true);

    Button refreshButton =
            this.addRenderableWidget(Button.builder(Component.literal("⟳"), (button) -> refresh(true))
                    .width(20)
                    .tooltip(Tooltip.create(Component.literal("Refresh current view")))
                    .build());

    Button createVersionButton =
            this.addRenderableWidget(Button.builder(Component.literal("Create version"), (button) -> {
              assert this.minecraft != null;
              this.minecraft.setScreen(new NewVersionScreen(parentScreen));
            }).width(100).tooltip(Tooltip.create(Component.literal("Create a new version"))).build());

    EqualSpacingLayout buttonLayout = new EqualSpacingLayout(this.width - (2 * this.width / 10), 20,
            EqualSpacingLayout.Orientation.HORIZONTAL);
    buttonLayout.addChild(refreshButton);
    buttonLayout.addChild(createVersionButton);
    FrameLayout.centerInRectangle(buttonLayout, this.width / 10, CONTENT_Y + CONTENT_HEIGHT,
            this.width - (this.width / 10 * 2),
            FOOTER_HEIGHT);
    buttonLayout.arrangeElements();
  }

  private void refresh(boolean full) {
    try {
      if (full) {
        OptionsActionsContext.fullRefresh();
      } else {
        OptionsActionsContext.lightRefresh();
      }
      buildCenterLayout();
      buildHeadingLayout();
    } catch (SbcouException e) {
      throw new RuntimeException(e);
    }
  }

  public class ClickablePathElem extends AbstractWidget {
    StringWidget pathWidget;
    FlatKey key;

    public ClickablePathElem(int x, int y, Component message, FlatKey key) {
      super(x, y, 0, 9, message);
      this.key = key;
      this.pathWidget = new StringWidget(getX(), getY(), SbcouScreen.this.font.width(message.getVisualOrderText()), 9,
              message,
              Minecraft.getInstance().font);
      this.width = pathWidget.getWidth();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      GlStateManager._enableBlend();
      GlStateManager._enableDepthTest();
      if (isMouseOver(mouseX, mouseY)) {
        guiGraphics.fill(getX() - 2, getY() - 2, getX() + width + 2, getY() + height + 2, 0x7fffffff);
      }
      pathWidget.setPosition(getX(), getY());
      pathWidget.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput ignored) {
    }

    @Override
    public void onClick(double p_93371_, double p_93372_) {
      this.onPress();
    }

    @Override
    public boolean keyPressed(int p_93374_, int p_93375_, int p_93376_) {
      if (this.active && this.visible) {
        if (CommonInputs.selected(p_93374_)) {
          this.playDownSound(Minecraft.getInstance().getSoundManager());
          this.onPress();
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    public void onPress() {
      currentKey = key;
      refresh(false);
    }
  }

  private void buildHeadingLayout() {
    this.headingWidgets.forEach(this::removeWidget);
    this.headingWidgets.clear();
    int headingY = CONTENT_Y - 20;
    int headingHeight = 20;
    List<FlatKey> parents = currentKey.getAllParentsAsFlatKeys();
    LinearLayout centerLayout =
            new LinearLayout(layoutWidth, headingHeight, LinearLayout.Orientation.HORIZONTAL).spacing(5);
    centerLayout.setPosition(screenBorderWidth, headingY);
    Component separator = Component.literal("/");
    Component root = Component.literal("√");
    ClickablePathElem rootWidget = new ClickablePathElem(0, 0, root, FlatKey.EMPTY);
    headingWidgets.add(rootWidget);
    for (FlatKey parent : parents) {
      StringWidget separatorWidget = new StringWidget(0, 0, SbcouScreen.this.font.width(separator.getVisualOrderText()),
              9, separator, this.font);
      ClickablePathElem pathElem = new ClickablePathElem(0, 0, Component.literal(parent.getName()), parent);
      headingWidgets.add(separatorWidget);
      headingWidgets.add(pathElem);
    }
    StringWidget separatorWidget = new StringWidget(0, 0, SbcouScreen.this.font.width(separator.getVisualOrderText()),
            9, separator, this.font);
    headingWidgets.add(separatorWidget);
    if (!currentKey.isEmpty()) {
      ClickablePathElem pathElem = new ClickablePathElem(0, 0, Component.literal(currentKey.getName()), currentKey);
      headingWidgets.add(pathElem);
    }
    for (AbstractWidget widget : headingWidgets) {
      centerLayout.addChild(widget);
      this.addRenderableWidget(widget);
    }
    centerLayout.arrangeElements();
  }

  private void buildCenterLayout() throws SbcouException {
    if (this.entryList != null) {
      this.removeWidget(this.entryList);
    }

    // Handle deleted entries and filter ignored options
    List<FlatKeyValuePair> filteredKeys = new ArrayList<>();

    // Add non-ignored keys
    OptionsActionsContext.getOptionChangeNatureMap().entrySet().stream()
            .filter(e -> e.getKey().startsWith(currentKey.toString()) &&
                    new FlatKey(e.getKey()).getSize() == currentKey.getSize() + 1)
            .forEach(e -> {
              String key = e.getKey();
              OptionChange changeNature = e.getValue();

              // Skip if permanently ignored
              try {
                if (OptionsActionsUtils.getIgnoredOptionPaths().contains(key)) {
                  return;
                }
              } catch (OptionsActionsUtilsException ex) {
                throw new RuntimeException(ex);
              }

              // Skip if temporarily ignored
              if (OptionsActionsContext.getStateMap().getOrDefault(key, "unknown")
                      .equalsIgnoreCase(OptionHandlingState.IGNORE.toString())) {
                return;
              }

              // Skip if change ignored
              try {
                Map<String, Object> ignoredChanges = OptionsActionsUtils.getIgnoredChange();
                if (ignoredChanges.containsKey(key)) {
                  if ((changeNature.equals(OptionChange.DELETED) && ignoredChanges.get(key).equals("sbcou:deleted")) ||
                          (ignoredChanges.get(key).equals(OptionsActionsContext.getMainContextFlatObject().get(key)))) {
                    return;
                  }
                  ignoredChanges.remove(key);
                  OptionsActionsUtils.saveIgnoredChange(ignoredChanges);
                }
              } catch (OptionsActionsUtilsException ex) {
                throw new RuntimeException(ex);
              }

              if (changeNature == OptionChange.DELETED) {
                filteredKeys.add(new FlatKeyValuePair(key, "sbcou:deleted"));
              } else {
                filteredKeys.add(new FlatKeyValuePair(key, OptionsActionsContext.getMainContextFlatObject().get(key)));
              }
            });

    // Sort filteredKeys alphabetically by key
    filteredKeys.sort((a, b) -> a.getKey().getName().compareToIgnoreCase(b.getKey().getName()));

    entryList = this.addRenderableWidget(
            new EntryList(this.minecraft, layoutWidth,
                    CONTENT_HEIGHT,
                    CONTENT_Y, 20,
                    filteredKeys));
    FrameLayout.centerInRectangle(entryList, screenBorderWidth, CONTENT_Y, this.width - (screenBorderWidth * 2),
            CONTENT_HEIGHT);
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  private class EntryList extends AbstractSelectionList<EntryList.Entry> {
    public EntryList(net.minecraft.client.Minecraft minecraft, int width, int height, int y0, int y1,
                     List<FlatKeyValuePair> flatKeyValuePairs) {
      super(minecraft, width, height, y0, y1);
      for (FlatKeyValuePair entry : flatKeyValuePairs) {
        OptionChange changeNature =
                OptionsActionsContext.getOptionChangeNatureMap().getOrDefault(entry.getKey().toString(),
                        OptionChange.EQUAL);
        if (changeNature == OptionChange.EQUAL) {
          continue;
        }
        OptionHandlingState state = OptionHandlingState.valueOf(
                OptionsActionsContext.getStateMap().getOrDefault(entry.getKey().toString(), "unknown").toUpperCase());
        this.addEntry(new Entry(entry.getKey(), entry.getValue(), changeNature, state));
      }
      this.centerListVertically = false;
    }

    //@Override
    //protected int getDefaultScrollbarPosition() {
    //  return this.getRealRowRight() + getScrollBarOffset();
    //}

    private int getScrollBarOffset() {
      return 4;
    }

    private int getRealRowRight() {
      return this.getRealRowLeft() + this.getRowWidth();
    }

    private int getRealRowLeft() {
      return this.getX() + this.width / 2 - this.getRowWidth() / 2;
    }

    @Override
    public int getRowWidth() {
      return this.width - 20;
    }

    @Override
    public Entry getSelected() {
      return null;
    }

    public void updateWidgetNarration(@NotNull NarrationElementOutput p_169042_) {
      EntryList.Entry e = this.getHovered();
      if (e != null) {
        this.narrateListElementPosition(p_169042_.nest(), e);
        e.updateNarration(p_169042_);
      } else {
        EntryList.Entry e1 = this.getSelected();
        if (e1 != null) {
          this.narrateListElementPosition(p_169042_.nest(), e1);
          e1.updateNarration(p_169042_);
        }
      }

      if (this.isFocused()) {
        p_169042_.add(NarratedElementType.USAGE, getUsageNarration());
      }
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> implements
            ContainerEventHandler {
      // Entry fields
      private final FlatKey key;
      private final Object value;
      private OptionHandlingState state;
      private final String name;

      // Main menu buttons
      private Button subLevelButton;
      private Button buttonInclude;
      private Button buttonIgnore;
      private Button buttonReset;

      // Include menu buttons
      private Button buttonDefault;
      private Button buttonOverride;

      // GUI var
      @Nullable
      private GuiEventListener focused;
      private boolean dragging;
      private final StringWidget changeSymbolWidget;
      private final StringWidget nameWidget;
      private final StringWidget stateWidget;
      private boolean stateChanged;
      private boolean nested = false;
      private Button nestedButton;
      private StringWidget nestedOverlayWidget;
      private int buttonX;
      private MenuLevel menu;
      List<GuiEventListener> mainMenuButtons = new ArrayList<>();
      List<GuiEventListener> includeMenuButtons = new ArrayList<>();
      private Button buttonIgnoreDef;
      private Button buttonIgnoreChange;
      private Button buttonTmpIgnore;
      List<GuiEventListener> ignoreMenuButtons = new ArrayList<>();

      public Entry(FlatKey key, Object value, OptionChange optionChange, OptionHandlingState state) {
        this.key = key;
        this.value = value;
        this.state = state;
        this.name = key.getName();

        createMainMenuButtons();
        createIncludeButtons();
        createIgnoreButtons();

        String changeSymbol;
        int changeColor;
        String tooltipText;
        switch (optionChange) {
          case OptionChange.NEW:
            changeSymbol = "+ ";
            changeColor = 0x00FF00; // Green
            tooltipText = "New element";
            break;
          case OptionChange.DELETED:
            changeSymbol = "- ";
            changeColor = 0xFF0000; // Red
            tooltipText = "Deleted element";
            break;
          case OptionChange.MODIFIED:
            changeSymbol = "► ";
            changeColor = 0xFFA500; // Orange
            tooltipText = "Modified element";
            break;
          default:
            changeSymbol = "";
            changeColor = 0xFFFFFF; // White by default
            tooltipText = "No change";
        }
        Component changeSymbolComponent = Component.literal(changeSymbol);
        this.changeSymbolWidget = new StringWidget(0, 0,
                SbcouScreen.this.font.width(changeSymbolComponent.getVisualOrderText()), 9, changeSymbolComponent,
                SbcouScreen.this.font);
        this.changeSymbolWidget.setColor(changeColor);
        this.changeSymbolWidget.setTooltip(Tooltip.create(Component.literal(tooltipText)));

        Component nameComponent = Component.literal(getName());
        this.nameWidget = new StringWidget(0, 0,
                SbcouScreen.this.font.width(nameComponent.getVisualOrderText()), 9,
                nameComponent,
                SbcouScreen.this.font);
        this.nameWidget.setColor(0xFFFFFF);
        this.nameWidget.alignLeft();
        this.nameWidget.setTooltip(Tooltip.create(nameComponent));

        this.stateWidget = new StringWidget(0, 0, 0, 9, Component.empty(), SbcouScreen.this.font);
        stateChanged = true;

        if (value instanceof Map) {
          this.nested =
                  OptionsActionsContext.getStateMap().keySet().stream()
                          .anyMatch(k -> k.startsWith(key.toString()) && !k.equals(key.toString()));
        }

        this.menu = MenuLevel.MAIN; // Initialize menu to MAIN
      }

      private static Button createMenuButton(String label, String tooltip, Button.OnPress onPress) {
        return Button.builder(Component.literal(label), onPress)
                .width(20)
                .size(20, 20)
                .tooltip(Tooltip.create(Component.literal(tooltip)))
                .build();
      }

      private void createIncludeButtons() {
        this.buttonDefault =
                createMenuButton("✔", "Set change to default", (ignored) -> this.setState(OptionHandlingState.DEFAULT));
        includeMenuButtons.add(this.buttonDefault);

        this.buttonOverride =
                createMenuButton("✎", "Override with change", (ignored) -> this.setState(OptionHandlingState.OVERRIDE));
        includeMenuButtons.add(this.buttonOverride);
      }

      private void createMainMenuButtons() {
        this.buttonReset =
                createMenuButton("O", "Reset state", (ignored) -> this.setState(OptionHandlingState.UNKNOWN));
        mainMenuButtons.add(this.buttonReset);

        this.buttonInclude = createMenuButton("✔", "Include", (ignored) -> menu = MenuLevel.INCLUDE);
        mainMenuButtons.add(this.buttonInclude);

        this.buttonIgnore = createMenuButton("✖", "Ignore", (ignored) -> menu = MenuLevel.IGNORE);
        mainMenuButtons.add(this.buttonIgnore);

        this.nestedButton = createMenuButton("\u2610", "Toggle nested handling", (ignored) -> toggleNested());
        this.nestedOverlayWidget = new StringWidget(0, 0, 20, 20, Component.literal("☷"), SbcouScreen.this.font);
        this.mainMenuButtons.add(this.nestedButton);

        this.subLevelButton = createMenuButton("▼", "Open sub-level", (ignored) -> {
          currentKey = this.key;
          refresh(false);
        });
        mainMenuButtons.add(this.subLevelButton);
      }

      private void createIgnoreButtons() {
        this.buttonIgnoreDef = createMenuButton("D", "Permanently ignore this option", (ignored) -> ignoreDef());
        ignoreMenuButtons.add(this.buttonIgnoreDef);

        this.buttonIgnoreChange = createMenuButton("C", "Ignore this change", (ignored) -> ignoreChange());
        ignoreMenuButtons.add(this.buttonIgnoreChange);

        this.buttonTmpIgnore = createMenuButton("T", "Ignore for this version", (ignored) -> tmpIgnoreOption());
        ignoreMenuButtons.add(this.buttonTmpIgnore);
      }

      private void toggleNested() {
        if (this.nested) {
          // If nested is currently true and is being set to false, show the warning screen
          assert SbcouScreen.this.minecraft != null;
          SbcouScreen.this.minecraft.setScreen(new GenericWarningScreen(SbcouScreen.this, Component.literal(
                  "Warning"),
                  Component.literal("Disabling nested handling will remove all nested elements individual handling" +
                          ". Are you sure you want to proceed?"),
                  this::resetStateRecurse));
        } else {
          this.nested = true;
          this.nestedOverlayWidget.active = true;
        }
      }

      private void ignoreDef() {
        assert SbcouScreen.this.minecraft != null;
        SbcouScreen.this.minecraft.setScreen(
                new GenericWarningScreen(SbcouScreen.this, Component.literal("Warning"),
                        Component.literal(
                                "This action will forever ignore this option path changes and all states of " +
                                        "sub-elements. Are you sure you want to proceed?"),
                        () -> {
                          this.resetStateRecurse();
                          try {
                            List<String> ignoredChanges = OptionsActionsUtils.getIgnoredOptionPaths();
                            ignoredChanges.add(this.key.toString());
                            OptionsActionsUtils.saveIgnoredOptionPaths(ignoredChanges);
                          } catch (OptionsActionsUtilsException e) {
                            throw new RuntimeException("Failed to update ignored changes", e);
                          }
                        }));
      }

      private void ignoreChange() {
        assert SbcouScreen.this.minecraft != null;
        SbcouScreen.this.minecraft.setScreen(new GenericWarningScreen(SbcouScreen.this, Component.literal("Warning"),
                Component.literal("This action will ignore this change. This option will not appear until an other " +
                        "change is made. All the changes of sub-elements will be reset as well. Are you sure you " +
                        "want to proceed?"),
                () -> {
                  this.resetStateRecurse();
                  try {
                    Map<String, Object> ignoredChanges = OptionsActionsUtils.getIgnoredChange();
                    ignoredChanges.put(this.key.toString(), this.value);
                    OptionsActionsUtils.saveIgnoredChange(ignoredChanges);
                  } catch (OptionsActionsUtilsException e) {
                    throw new RuntimeException("Failed to update ignored changes", e);
                  }
                }));
      }

      private void tmpIgnoreOption() {
        assert SbcouScreen.this.minecraft != null;
        SbcouScreen.this.minecraft.setScreen(new GenericWarningScreen(SbcouScreen.this, Component.literal("Warning"),
                Component.literal("This action will temporarily ignore this option until the next version is created." +
                        " The state will be reset for this element and all its sub-elements. Are you sure you want to" +
                        " proceed?"),
                () -> {
                  this.resetStateRecurse();
                  this.setState(OptionHandlingState.IGNORE);
                }));
      }

      private void updateStateWidget() {
        if (!stateChanged) {
          return;
        }
        String stateIcon = switch (state) {
          case OptionHandlingState.DEFAULT -> "✔";
          case OptionHandlingState.IGNORE -> "✖";
          case OptionHandlingState.UNKNOWN -> "?";
          case OptionHandlingState.NESTED_CASE -> "☷";
          case OptionHandlingState.OVERRIDE -> "✎";
        };
        String stateText = switch (state) {
          case OptionHandlingState.DEFAULT -> "Default";
          case OptionHandlingState.IGNORE -> "Ignore";
          case OptionHandlingState.UNKNOWN -> "Unknown";
          case OptionHandlingState.NESTED_CASE -> "Nested";
          case OptionHandlingState.OVERRIDE -> "Override";
        };
        int stateColor = switch (state) {
          case OptionHandlingState.DEFAULT -> 0x00FF00; // Green
          case OptionHandlingState.IGNORE -> 0xFF0000; // Red
          case OptionHandlingState.UNKNOWN -> 0x808080; // Gray
          case OptionHandlingState.NESTED_CASE -> 0xFFA500; // Orange
          case OptionHandlingState.OVERRIDE -> 0x0000FF; // Blue
        };
        this.stateWidget.setColor(stateColor);
        this.stateWidget.setMessage(Component.literal(stateIcon));
        this.stateWidget.setTooltip(Tooltip.create(Component.literal(stateText)));
        this.stateWidget.setWidth(20);
        stateChanged = false;
      }

      public @NotNull Component getNarration() {
        return Component.translatable("narrator.select", this.getName());
      }

      public void updateNarration(NarrationElementOutput p_169044_) {
        p_169044_.add(NarratedElementType.TITLE, this.getNarration());
      }

      public String getName() {
        return name;
      }

      public void setState(OptionHandlingState state) {
        try {
          OptionsActionsContext.setState(key.toString(), state);
        } catch (OptionsActionsContextException e) {
          throw new RuntimeException("Failed to save state", e);
        }
        this.state = state;
        this.stateChanged = true;
      }

      public void resetStateRecurse() {
        this.setState(OptionHandlingState.UNKNOWN);
        Map<String, String> stateMap = new HashMap<>(OptionsActionsContext.getStateMap());
        stateMap.keySet().stream()
                .filter(key -> key.startsWith(this.key.toString()))
                .forEach(key -> {
                  try {
                    OptionsActionsContext.setState(key, OptionHandlingState.UNKNOWN);
                  } catch (OptionsActionsContextException e) {
                    throw new RuntimeException("Failed to save state", e);
                  }
                });
      }

      @Override
      public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height,
                         int mouseX,
                         int mouseY, boolean isMouseOver, float partialTick) {
        // Reset menu to MAIN if the mouse is not over the entry
        if (!isMouseOver) {
          this.menu = MenuLevel.MAIN;
        }

        // Update buttonX before rendering the main menu
        this.buttonX = left + width - 25;

        // Update change symbol
        this.changeSymbolWidget.setPosition(left + 5, top + 5);
        this.changeSymbolWidget.render(guiGraphics, mouseX, mouseY, partialTick);

        // Update state widget (always update position as it is a reference for other widgets)
        this.stateWidget.setPosition(left + width - 175, top + 5);
        if (!(value instanceof Map) || this.state != OptionHandlingState.UNKNOWN) {
          this.updateStateWidget();
          this.stateWidget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        // Update name widget
        this.nameWidget.setPosition(left + 20, top + 5);
        this.nameWidget.setWidth(this.stateWidget.getX() - this.nameWidget.getX());
        this.nameWidget.render(guiGraphics, mouseX, mouseY, partialTick);

        switch (this.menu) {
          case MAIN -> renderMainMenu(guiGraphics, left, top, width, mouseX, mouseY, partialTick);
          case INCLUDE -> renderIncludeMenu(guiGraphics, left, top, width, mouseX, mouseY);
          case IGNORE -> renderIgnoreMenu(guiGraphics, left, top, width, mouseX, mouseY);
        }
      }

      private void renderMainMenu(@NotNull GuiGraphics guiGraphics, int left, int top, int width,
                                  int mouseX, int mouseY, float partialTick) {
        this.buttonReset.setPosition(buttonX, top);
        this.buttonReset.active = (state != OptionHandlingState.UNKNOWN);
        this.buttonReset.render(guiGraphics, mouseX, mouseY, partialTick);

        this.buttonIgnore.setPosition(buttonReset.getX() - 25, top);
        this.buttonIgnore.active = (state != OptionHandlingState.IGNORE);
        this.buttonIgnore.render(guiGraphics, mouseX, mouseY, partialTick);

        this.buttonInclude.setPosition(buttonIgnore.getX() - 25, top);
        this.buttonInclude.render(guiGraphics, mouseX, mouseY, partialTick);

        if (value instanceof Map) {
          this.nestedButton.setPosition(buttonInclude.getX() - 25, top);
          this.nestedButton.render(guiGraphics, mouseX, mouseY, partialTick);

          if (this.nested) {
            this.nestedOverlayWidget.setPosition(buttonInclude.getX() - 25, top + 1);
            this.nestedOverlayWidget.render(guiGraphics, mouseX, mouseY, partialTick);

            this.subLevelButton.setPosition(nestedButton.getX() - 25, top);
            this.subLevelButton.render(guiGraphics, mouseX, mouseY, partialTick);
          }
        }
      }

      private void renderIncludeMenu(@NotNull GuiGraphics guiGraphics, int left, int top, int width,
                                     int mouseX, int mouseY) {
        this.buttonDefault.setPosition(buttonX, top);
        this.buttonDefault.active = (state != OptionHandlingState.DEFAULT);
        this.buttonDefault.render(guiGraphics, mouseX, mouseY, 0);

        this.buttonOverride.setPosition(buttonDefault.getX() - 25, top);
        this.buttonOverride.active = (state != OptionHandlingState.OVERRIDE);
        this.buttonOverride.render(guiGraphics, mouseX, mouseY, 0);
      }

      private void renderIgnoreMenu(@NotNull GuiGraphics guiGraphics, int left, int top, int width,
                                    int mouseX, int mouseY) {
        this.buttonIgnoreDef.setPosition(buttonX, top);
        this.buttonIgnoreDef.render(guiGraphics, mouseX, mouseY, 0);

        this.buttonIgnoreChange.setPosition(buttonIgnoreDef.getX() - 25, top);
        this.buttonIgnoreChange.render(guiGraphics, mouseX, mouseY, 0);

        this.buttonTmpIgnore.setPosition(buttonIgnoreChange.getX() - 25, top);
        this.buttonTmpIgnore.render(guiGraphics, mouseX, mouseY, 0);
      }

      @Override
      public @NotNull List<? extends GuiEventListener> children() {
        return switch (this.menu) {
          case MAIN -> {
            List<GuiEventListener> children = new ArrayList<>(mainMenuButtons);
            if (this.nested) {
              children.add(this.nestedButton);
              children.add(this.nestedOverlayWidget);
              children.add(this.subLevelButton);
            }
            yield children;
          }
          case INCLUDE -> includeMenuButtons;
          case IGNORE -> ignoreMenuButtons;
        };
      }

      @Override
      public boolean isDragging() {
        return this.dragging;
      }

      @Override
      public void setDragging(boolean pDragging) {
        this.dragging = pDragging;
      }

      @Nullable
      @Override
      public GuiEventListener getFocused() {
        return this.focused;
      }

      public void setFocused(@Nullable GuiEventListener pListener) {
        if (pListener != null) {
          pListener.setFocused(false);
          this.focused = pListener;
        }
      }

      // Define the MenuLevel enum
      private enum MenuLevel {
        MAIN,
        INCLUDE,
        IGNORE
      }
    }
  }
}
