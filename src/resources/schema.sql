PRAGMA foreign_keys = ON;

CREATE TABLE category(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE combatant(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    max_hp INTEGER NOT NULL,
    armor_class INTEGER NOT NULL,
    category_id INTEGER NOT NULL REFERENCES category(id)
);

CREATE TABLE creature_type(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE combatant_creature_type(
    combatant_id INTEGER NOT NULL REFERENCES combatant(id),
    type_id INTEGER NOT NULL REFERENCES creature_type(id),
    PRIMARY KEY (combatant_id, type_id)
);

-- Seed Default Categories and Types
INSERT OR IGNORE INTO category(name) VALUES
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

