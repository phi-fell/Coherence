package io.proffitt.coherence.world;

public class MobAttributes {
	//**************************************************
	//attributes
	//**************************************************
	public static final int	ATTR_DEFAULT_VALUE				= 10;
	public static final int	ATTR_NUM						= 8;
	public static final int	ATTR_STRENGTH					= 0;
	public static final int	ATTR_DEXTERITY					= 1;
	public static final int	ATTR_AGILITY					= 2;
	public static final int	ATTR_ENDURANCE					= 3;
	public static final int	ATTR_INTELLIGENCE				= 4;
	public static final int	ATTR_WISDOM						= 5;
	public static final int	ATTR_CHARISMA					= 6;
	public static final int	ATTR_FAITH						= 7;
	//**************************************************
	//skills
	//**************************************************
	public static final int	SKILL_DEFAULT_VALUE				= 0;
	public static final int	SKILL_NUM						= 8;				//TODO: total count of skills
	//weapon skills
	public static final int	SKILL_BLUNT						= 0;
	public static final int	SKILL_SHORT_BLADE				= 0;
	public static final int	SKILL_LONG_1H_BLADE				= 0;
	public static final int	SKILL_LONG_2H_BLADE				= 0;
	public static final int	SKILL_AXE						= 0;
	public static final int	SKILL_SPEAR						= 0;
	public static final int	SKILL_POLEARM					= 0;
	public static final int	SKILL_BOW						= 0;
	public static final int	SKILL_CROSSBOW					= 0;
	//will magic
	public static final int	SKILL_WILL_MAGIC				= 0;
	public static final int	SKILL_CREATION					= 0;
	public static final int	SKILL_ALTERATION				= 0;
	public static final int	SKILL_DESTRUCTION				= 0;
	public static final int	SKILL_BINDING					= 0;				//i.e. summoning: binding an entity to one's will
	//sigil magic
	public static final int	SKILL_SIGIL_MAGIC				= 0;
	public static final int	SKILL_SOUL_BINDING				= 0;
	//stealth
	public static final int	SKILL_STEALTH					= 0;
	public static final int	SKILL_PICKPOCKET				= 0;
	public static final int	SKILL_LOCKPICKING				= 0;
	//NPC interactions
	public static final int	SKILL_BARTERING					= 0;
	public static final int	SKILL_PERSUASION				= 0;
	public static final int	SKILL_INTIMIDATION				= 0;				//DESIGN: merge with persuasion?
	//faith
	public static final int	SKILL_PRAYER					= 0;				//gain favor with a deity
	public static final int	SKILL_CHANNELING				= 0;				//channel a deities will/power for your own use
	public static final int	SKILL_MANIFESTATION				= 0;				//manifest a deity to help you (i.e. summon part or all of a deity)
	public static final int	SKILL_EMBODIMENT				= 0;				//take on attributes of a deity (increased damage, faster healing, aversion to certain elements, etc.)
	//general
	public static final int	SKILL_MOVEMENT					= 0;				//DESIGN: should this be a thing?
	public static final int	SKILL_SWIMMING					= 0;				//DESIGN: should this be a thing?
	public static final int	SKILL_HERBALISM					= 0;
	public static final int	SKILL_ALCHEMY					= 0;				//DESIGN: should this be named alchemy, or something else?
	//**************************************************
	//deities
	//**************************************************
	public static final int	DEITY_NUM						= 24;
	public static final int	DEITY_DEFAULT_VALUE				= 0;
	//TODO: add deities origination from beyond world created by DEITY_ARCHITECT, besides wanderer (whose origin should remain ambiguous)?
	//Dark gods
	public static final int	DEITY_FIRST						= 0;
	public static final int	DEITY_ARCHITECT					= 0;
	public static final int	DEITY_DEMIURGE_1				= 0;				//DESIGN/TODO: add more gods?
	public static final int	DEITY_DEMIURGE_2				= 0;				// make overall hierarchy more chaotic/organic?
	public static final int	DEITY_DEMIURGE_3				= 0;
	public static final int	DEITY_ASPECT_1					= 0;
	public static final int	DEITY_ASPECT_2					= 0;
	public static final int	DEITY_ASPECT_3					= 0;
	public static final int	DEITY_ASPECT_4					= 0;
	public static final int	DEITY_ASPECT_5					= 0;
	public static final int	DEITY_ASPECT_6					= 0;
	public static final int	DEITY_ASPECT_7					= 0;
	public static final int	DEITY_ASPECT_8					= 0;
	public static final int	DEITY_ASPECT_9					= 0;
	public static final int	DEITY_WANDERER					= 0;
	public static final int	DEITY_ELEMENTAL_DARK			= 0;
	public static final int	DEITY_ELEMENTAL_EARTH			= 0;
	public static final int	DEITY_ELEMENTAL_WATER			= 0;
	public static final int	DEITY_ELEMENTAL_WIND			= 0;
	public static final int	DEITY_ELEMENTAL_FIRE			= 0;
	public static final int	DEITY_ELEMENTAL_LIGHT			= 0;
	//deified mortals
	public static final int	DEITY_MAGE_WILL					= 0;
	public static final int	DEITY_MAGE_SIGIL				= 0;
	public static final int	DEITY_MAGE_SHADOW				= 0;				//TODO: add more mage gods and various apotheons
	//TODO: add minor gods here
	//**************************************************
	//factions
	//**************************************************
	public static final int	FACTION_NUM						= 6;				//TODO
	public static final int	FACTION_DEFAULT_VALUE			= 0;
	//empires/countries/city states/what have you
	public static final int	FACTION_SOME_EMPIRE_GOES_HERE	= 0;				//DESIGN: should factions be an array of reputation, or allow string identifiers so as too be better able to dynamically generate new factions (this may depend greatly on how much of the game world is procedural)
	//guild factions
	public static final int	FACTION_GUILD_SIGIL				= 0;				//DESIGN/TODO: there should eventually not be a single 'Mage's Guild' or 'Thief's Guild',
	public static final int	FACTION_GUILD_THIEF				= 0;				//		instead there will be various competing factions, some existing only in a single town/city,
	public static final int	FACTION_GUILD_MERCENARY			= 0;				//		some stretching over entire (or multiple) empires (and many somewhere in between)
	//monster factions
	public static final int	FACTION_MONSTER_BEAST			= 0;				//DESIGN: not all beasts should be aligned, but most, if not all, should attack the player,
	public static final int	FACTION_MONSTER_DENIZEN			= 0;				//they also should in general attack whatever seems most dangerous (often player) or run.
	public static final int	FACTION_MONSTER_ELEMENTAL		= 0;				//denizens should act similarly, but with more cohesion (but still some infighting).  denizens should probably also be more likely to flee from a vastly superior foe
	//DESIGN: for reference:
	//beasts: wolves, giant serpents, anything that thinks like an animal and isn't *too* supernatural
	//denizens (DESIGN: better name?): goblins, ogres, some undead (not bound to a mage, nonsentient), etc.
	//actual data
	private int[]			attributes						= new int[ATTR_NUM];
	{
		for (int i = 0; i < ATTR_NUM; i++) {
			attributes[i] = ATTR_DEFAULT_VALUE;
		}
	}
	private int[] skills = new int[SKILL_NUM];
	{
		for (int i = 0; i < SKILL_NUM; i++) {
			skills[i] = SKILL_DEFAULT_VALUE;
		}
	}
	private int[] favor = new int[DEITY_NUM];
	{
		for (int i = 0; i < DEITY_NUM; i++) {
			favor[i] = DEITY_DEFAULT_VALUE;
		}
	}
	private int[] reputation = new int[FACTION_NUM];
	{
		for (int i = 0; i < FACTION_NUM; i++) {
			reputation[i] = FACTION_DEFAULT_VALUE;
		}
	}
}
