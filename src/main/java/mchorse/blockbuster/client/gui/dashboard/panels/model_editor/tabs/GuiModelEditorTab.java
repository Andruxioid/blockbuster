package mchorse.blockbuster.client.gui.dashboard.panels.model_editor.tabs;

import mchorse.blockbuster.client.gui.dashboard.panels.model_editor.GuiModelEditorPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiModelEditorTab extends GuiElement
{
    protected IKey title = IKey.EMPTY;
    protected GuiModelEditorPanel panel;

    public GuiModelEditorTab(Minecraft mc, GuiModelEditorPanel panel)
    {
        super(mc);

        this.panel = panel;
    }

    public GuiModelEditorPanel getPanel()
    {
        return this.panel;
    }

    @Override
    public void draw(GuiContext context)
    {
        this.drawLabels();
        super.draw(context);
    }

    protected void drawLabels()
    {
        if (this.title != null)
        {
            this.font.drawStringWithShadow(this.title.get(), this.area.x + 4, this.area.y + 6, 0xeeeeee);
        }
    }
}