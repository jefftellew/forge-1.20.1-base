package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpinAttackEffectLayer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");
   public static final String BOX = "box";
   private final ModelPart box;

   public SpinAttackEffectLayer(RenderLayerParent<T, PlayerModel<T>> pRenderer, EntityModelSet pModelSet) {
      super(pRenderer);
      ModelPart modelpart = pModelSet.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
      this.box = modelpart.getChild("box");
   }

   public static LayerDefinition createLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      partdefinition.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
      if (pLivingEntity.isAutoSpinAttack()) {
         VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

         for(int i = 0; i < 3; ++i) {
            pMatrixStack.pushPose();
            float f = pAgeInTicks * (float)(-(45 + i * 5));
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(f));
            float f1 = 0.75F * (float)i;
            pMatrixStack.scale(f1, f1, f1);
            pMatrixStack.translate(0.0F, -0.2F + 0.6F * (float)i, 0.0F);
            this.box.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pMatrixStack.popPose();
         }

      }
   }
}