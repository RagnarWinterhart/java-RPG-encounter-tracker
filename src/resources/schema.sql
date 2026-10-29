PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS categories(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS combatants(
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    max_hp INTEGER NOT NULL,
    armor_class INTEGER NOT NULL,
    category_id INTEGER NOT NULL REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS creature_type(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS combatant_creature_type(
    combatant_id INTEGER NOT NULL REFERENCES combatant(id),
    type_id INTEGER NOT NULL REFERENCES creature_type(id),
    PRIMARY KEY (combatant_id, type_id)
);

CREATE TABLE IF NOT EXISTS pf2e_stat_block(
    id INTEGER PRIMARY KEY,
    level INTEGER,
    perception INTEGER,
    fortitude INTEGER,
    reflex INTEGER,
    will INTEGER,
    speed INTEGER,
    combatant_id INTEGER PRIMARY KEY REFERENCES combatant(id)
);

-- Seed Default Categories and Types
INSERT OR IGNORE INTO categories(name) VALUES
    ('Monster'),
    ('NPC'),
    ('Player');

INSERT OR IGNORE INTO creature_type(name) VALUES
    ('Dragon'),
    ('Undead'),
    ('Humanoid'),
    ('Beast'),
    ('Construct'),
    ('Elemental'),
    ('Fiend'),
    ('Giant'),
    ('Monstrosity'),
    ('Plant');

