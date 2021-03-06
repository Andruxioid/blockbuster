package mchorse.blockbuster.client.gui.dashboard.panels.snowstorm.sections;

import mchorse.blockbuster.client.gui.dashboard.panels.snowstorm.GuiSnowstorm;
import mchorse.blockbuster.client.particles.BedrockScheme;
import mchorse.blockbuster.client.particles.components.motion.BedrockComponentMotionCollision;
import mchorse.blockbuster.client.particles.molang.MolangParser;
import mchorse.blockbuster.client.particles.molang.expressions.MolangExpression;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.client.gui.utils.Elements;
import net.minecraft.client.Minecraft;

public class GuiSnowstormCollisionSection extends GuiSnowstormComponentSection<BedrockComponentMotionCollision>
{
	public GuiToggleElement enabled;
	public GuiToggleElement realisticCollision;
	public GuiToggleElement entityCollision;
	public GuiToggleElement momentum;
	public GuiTrackpadElement drag;
	public GuiTrackpadElement bounciness;
	public GuiTrackpadElement randomBounciness; //randomize the direction vector
	public GuiToggleElement preserveEnergy;
	public GuiTrackpadElement randomDamp;
	public GuiTrackpadElement damp;
	public GuiTrackpadElement splitParticle; //split particle into n particles on collision
	public GuiTrackpadElement splitParticleSpeedThreshold;
	public GuiTrackpadElement radius;
	public GuiToggleElement expire;
	public GuiTextElement expirationDelay;
	
	public GuiElement controlToggleElements;
	public GuiElement randomBouncinessRow;

	private boolean wasPresent;

	public GuiSnowstormCollisionSection(Minecraft mc, GuiSnowstorm parent)
	{
		super(mc, parent);

		this.enabled = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.enabled"), (b) -> this.parent.dirty());
		
		this.realisticCollision = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.realisticCollision"), (b) ->
		{
			this.component.realisticCollision = b.isToggled();
			this.parent.dirty();
		});
		
		this.entityCollision = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.entityCollision"), (b) ->
		{
			this.component.entityCollision = b.isToggled();
			this.parent.dirty();
		});
		
		this.momentum = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.momentum"), (b) ->
		{
			this.component.momentum = b.isToggled();
			this.parent.dirty();
		});
		
		this.drag = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.collisionDrag = value.floatValue();
			this.parent.dirty();
		});
		this.drag.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.drag"));
		
		this.bounciness = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.bounciness = value.floatValue();
			this.parent.dirty();
		});
		this.bounciness.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.bounciness"));
		
		this.randomBounciness = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.randomBounciness = (float) Math.abs(value);
			this.parent.dirty();
		});
		this.randomBounciness.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.randomDirection"));
		
		this.preserveEnergy = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.preserveEnergy"), (b) -> this.parent.dirty());
		
		this.damp = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.damp = value.floatValue();
			this.parent.dirty();
		});
		this.damp.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.damping.strength"));
		this.damp.limit(0, 1);
		
		this.randomDamp = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.randomDamp = (float) Math.abs(value);
			this.parent.dirty();
		});
		this.randomDamp.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.damping.randomness"));
		this.randomDamp.limit(0, 1);
		
		this.splitParticleSpeedThreshold = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.splitParticleSpeedThreshold = value.floatValue();
			this.parent.dirty();
		});
		this.splitParticleSpeedThreshold.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.splitParticle.speedThreshold"));
		
		this.splitParticle = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.splitParticleCount = (int)Math.abs(value);
			this.parent.dirty();
		});
		this.splitParticle.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.splitParticle.count"));
		this.splitParticle.limit(0, 99);
		
		this.radius = new GuiTrackpadElement(mc, (value) ->
		{
			this.component.radius = value.floatValue();
			this.parent.dirty();
		});
		this.radius.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.radius"));
		
		this.expire = new GuiToggleElement(mc, IKey.lang("blockbuster.gui.snowstorm.collision.expire"), (b) ->
		{
			this.component.expireOnImpact = b.isToggled();
			this.parent.dirty();
		});
		
		this.expirationDelay = new GuiTextElement(mc, (value) ->
		{
			this.component.expirationDelay = this.parse(value, this.expirationDelay, this.component.expirationDelay);
			this.parent.dirty();
		});
		this.expirationDelay.tooltip(IKey.lang("blockbuster.gui.snowstorm.collision.expirationDelay"));
		
		this.controlToggleElements = new GuiElement(mc);
		this.controlToggleElements.flex().column(4).stretch().vertical().height(4);
		
		this.controlToggleElements.add(this.enabled, this.realisticCollision, this.entityCollision);
		
		this.randomBouncinessRow = new GuiElement(mc);
		this.randomBouncinessRow.flex().column(2).stretch().vertical().height(2);

		this.randomBouncinessRow.add(this.randomBounciness);
		
		this.fields.add(this.controlToggleElements, this.drag, this.bounciness, this.randomBouncinessRow , this.radius, this.expire, this.expirationDelay);
		this.fields.add(Elements.label(IKey.lang("blockbuster.gui.snowstorm.collision.damping.title"), 20).anchor(0, 1F), this.damp, this.randomDamp);
		this.fields.add(Elements.label(IKey.lang("blockbuster.gui.snowstorm.collision.splitParticle.title"), 20).anchor(0, 1F), this.splitParticle,  this.splitParticleSpeedThreshold);
	}

	@Override
	public String getTitle()
	{
		return "blockbuster.gui.snowstorm.collision.title";
	}

	@Override
	public void beforeSave(BedrockScheme scheme)
	{
		this.component.enabled = this.enabled.isToggled() ? MolangParser.ONE : MolangParser.ZERO;
		this.component.preserveEnergy = this.preserveEnergy.isToggled() ? MolangParser.ONE : MolangParser.ZERO;
	}

	@Override
	protected BedrockComponentMotionCollision getComponent(BedrockScheme scheme)
	{
		this.wasPresent = this.scheme.get(BedrockComponentMotionCollision.class) != null;

		return scheme.getOrCreate(BedrockComponentMotionCollision.class);
	}

	@Override
	protected void fillData()
	{
		this.preserveEnergy.removeFromParent();
		this.momentum.removeFromParent();
		
		this.enabled.toggled(MolangExpression.isOne(this.component.enabled));
		this.realisticCollision.toggled(this.component.realisticCollision);
		this.entityCollision.toggled(this.component.entityCollision);
		this.momentum.toggled(this.component.momentum);
		this.drag.setValue(this.component.collisionDrag);
		this.bounciness.setValue(this.component.bounciness);
		this.randomBounciness.setValue(this.component.randomBounciness);
		this.preserveEnergy.toggled(MolangExpression.isOne(this.component.preserveEnergy));
		this.damp.setValue(this.component.damp);
		this.randomDamp.setValue(this.component.randomDamp);
		this.splitParticle.setValue(this.component.splitParticleCount);
		this.splitParticleSpeedThreshold.setValue(this.component.splitParticleSpeedThreshold);
		this.radius.setValue(this.component.radius);
		this.expire.toggled(this.component.expireOnImpact);
		this.set(this.expirationDelay, this.component.expirationDelay);
		
		if (this.entityCollision.isToggled())
		{
			this.controlToggleElements.add(momentum);
		}
		
		if (this.randomBounciness.value!=0 && this.bounciness.value == 0)
		{
			this.randomBouncinessRow.add(preserveEnergy);
		}

		this.resizeParent();
	}
}
