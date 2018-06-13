package mchorse.blockbuster.recording.actions;

import mchorse.blockbuster.common.block.BlockDirector;
import mchorse.blockbuster.common.entity.EntityActor;
import mchorse.blockbuster.recording.data.Frame;
import mchorse.blockbuster.utils.EntityUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * Interact block action
 *
 * Makes actor interact with a block (press button, switch lever, open the door,
 * etc.)
 *
 * If there was CL4P-TP actor in this mod, this action would be called
 * IntergradeBlockAction :D
 */
public class InteractBlockAction extends Action
{
    public BlockPos pos;

    public InteractBlockAction()
    {}

    public InteractBlockAction(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public byte getType()
    {
        return Action.INTERACT_BLOCK;
    }

    @Override
    public void apply(EntityLivingBase actor)
    {
        IBlockState state = actor.world.getBlockState(this.pos);

        /* Black listed block */
        if (state.getBlock() instanceof BlockDirector)
        {
            return;
        }

        Frame frame = EntityUtils.getRecordPlayer(actor).getCurrentFrame();
        EntityPlayer player = actor instanceof EntityActor ? ((EntityActor) actor).fakePlayer : (EntityPlayer) actor;

        player.posX = actor.posX;
        player.posY = actor.posY;
        player.posZ = actor.posZ;
        player.rotationYaw = frame.yaw;
        player.rotationPitch = frame.pitch;
        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, actor.getHeldItemMainhand());
        player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, actor.getHeldItemOffhand());

        state.getBlock().onBlockActivated(actor.world, this.pos, state, player, EnumHand.MAIN_HAND, null, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public void changeOrigin(double rotation, double newX, double newY, double newZ, double firstX, double firstY, double firstZ)
    {
        /* I don't like wasting variables */
        firstX = this.pos.getX() - firstX;
        firstX = this.pos.getY() - firstY;
        firstX = this.pos.getZ() - firstZ;

        if (rotation != 0)
        {
            float cos = (float) Math.cos(rotation / 180 * Math.PI);
            float sin = (float) Math.sin(rotation / 180 * Math.PI);

            double xx = firstX * cos - firstZ * sin;
            double zz = firstX * sin + firstZ * cos;

            firstX = xx;
            firstZ = zz;
        }

        newX += firstX;
        newY += firstY;
        newZ += firstZ;

        this.pos = new BlockPos(newX, newY, newZ);
    }

    @Override
    public void fromNBT(NBTTagCompound tag)
    {
        this.pos = new BlockPos(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));
    }

    @Override
    public void toNBT(NBTTagCompound tag)
    {
        tag.setInteger("X", this.pos.getX());
        tag.setInteger("Y", this.pos.getY());
        tag.setInteger("Z", this.pos.getZ());
    }
}