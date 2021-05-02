package com.articreep.redactedpit.colosseum;

public enum AudienceEffect {
	
	INSTANT_HEAL(AudienceOpinion.POSITIVE), REGENERATION(AudienceOpinion.POSITIVE), ABSORPTION(AudienceOpinion.POSITIVE), INCREASED_GOLD(AudienceOpinion.POSITIVE),
	INCREASED_XP(AudienceOpinion.POSITIVE), STRENGTH(AudienceOpinion.POSITIVE), FIRE(AudienceOpinion.NEGATIVE), EXPLOSION(AudienceOpinion.NEGATIVE),
	RESISTANCE(AudienceOpinion.POSITIVE), WEAKNESS(AudienceOpinion.NEGATIVE), SMITE(AudienceOpinion.NEGATIVE), LARGEKB(AudienceOpinion.POSITIVE), BLOCKBUFF(AudienceOpinion.POSITIVE),
	BLASTED_AWAY(AudienceOpinion.POSITIVE), HOT_POTATO(AudienceOpinion.NEUTRAL);

	private final AudienceOpinion opinion;
	AudienceEffect(AudienceOpinion op) {
		opinion = op;
	}

	AudienceOpinion getOpinion() {
		return opinion;
	}

}
