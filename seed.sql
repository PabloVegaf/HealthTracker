-- =============================================
-- DATOS SINTÉTICOS PARA FITNESS_PROGRESS
-- =============================================

-- 1. USERS
INSERT INTO users (email, password_hash, name) VALUES
  ('pablo.garcia@email.com', '$2b$10$8K1p/a0dL1LXMIgoEDFrwOfMQkfAjkMBcGmF0J7FVuJFx6S0qO3iC', 'Pablo García'),
  ('ana.lopez@email.com', '$2b$10$8K1p/a0dL1LXMIgoEDFrwOfMQkfAjkMBcGmF0J7FVuJFx6S0qO3iC', 'Ana López');

-- 2. EXERCISE CATEGORIES (user 1 = Pablo, user 2 = Ana)
INSERT INTO exercise_categories (user_id, name) VALUES
  (1, 'Running'),
  (1, 'Weightlifting'),
  (1, 'Cycling'),
  (1, 'Yoga'),
  (2, 'Running'),
  (2, 'Swimming'),
  (2, 'Pilates'),
  (2, 'HIIT');

-- 3. DAILY RECORDS - PABLO (user_id = 1)
-- Evolución: 85 kg → 82 kg, 22% → 20% grasa corporal
-- Running (id=1) lunes/miércoles/viernes, Pesas (id=2) martes/jueves, Bici (id=3) sábados, Yoga (id=4) domingos
INSERT INTO daily_records (user_id, record_date, weight_kg, body_fat_pct, kcal_consumed, kcal_expended, exercise_category_id, exercise_duration_min, notes)
SELECT
  1,
  d::date,
  ROUND((85.0 - ((d::date - '2026-03-01'::date) * 3.0 / 77)), 1),
  ROUND((22.0 - ((d::date - '2026-03-01'::date) * 2.0 / 77)), 1),
  (1950 + (random() * 350)::int)::int,
  (2350 + (random() * 350)::int)::int,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 1 WHEN 3 THEN 1 WHEN 5 THEN 1  -- Running
    WHEN 2 THEN 2 WHEN 4 THEN 2                 -- Weightlifting
    WHEN 6 THEN 3                               -- Cycling
    WHEN 0 THEN 4                               -- Yoga
  END,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 28 + (random() * 12)::int
    WHEN 2 THEN 40 + (random() * 20)::int
    WHEN 3 THEN 30 + (random() * 10)::int
    WHEN 4 THEN 42 + (random() * 18)::int
    WHEN 5 THEN 30 + (random() * 15)::int
    WHEN 6 THEN 50 + (random() * 30)::int
    WHEN 0 THEN 55 + (random() * 15)::int
  END,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 'Semana arrancando con running'
    WHEN 3 THEN 'Entrenamiento de velocidad'
    WHEN 5 THEN 'Buena sesión de pesas'
    WHEN 6 THEN 'Ruta en bici por la mañana'
    WHEN 0 THEN 'Estiramientos y relajación'
    ELSE NULL
  END
FROM generate_series('2026-03-01'::date, '2026-05-16'::date, '1 day'::interval) d;

-- 4. DAILY RECORDS - ANA (user_id = 2)
-- Evolución: 65 kg → 64 kg, 25% → 24% grasa corporal (mantenimiento con leve déficit)
-- Running (id=5) lun/jue, Natación (id=6) mar, Pilates (id=7) mié/vie, HIIT (id=8) sáb
INSERT INTO daily_records (user_id, record_date, weight_kg, body_fat_pct, kcal_consumed, kcal_expended, exercise_category_id, exercise_duration_min, notes)
SELECT
  2,
  d::date,
  ROUND((65.0 - ((d::date - '2026-03-01'::date) * 1.0 / 77)), 1),
  ROUND((25.0 - ((d::date - '2026-03-01'::date) * 1.0 / 77)), 1),
  (1850 + (random() * 300)::int)::int,
  (1950 + (random() * 300)::int)::int,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 5 WHEN 4 THEN 5                 -- Running
    WHEN 2 THEN 6                               -- Swimming
    WHEN 3 THEN 7 WHEN 5 THEN 7                 -- Pilates
    WHEN 6 THEN 8                               -- HIIT
  END,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 25 + (random() * 15)::int
    WHEN 2 THEN 35 + (random() * 15)::int
    WHEN 3 THEN 45 + (random() * 10)::int
    WHEN 4 THEN 28 + (random() * 12)::int
    WHEN 5 THEN 40 + (random() * 15)::int
    WHEN 6 THEN 30 + (random() * 10)::int
  END,
  CASE EXTRACT(dow FROM d)
    WHEN 1 THEN 'Running suave'
    WHEN 2 THEN 'Natación en piscina'
    WHEN 3 THEN 'Pilates reparador'
    WHEN 4 THEN 'Series de running'
    WHEN 5 THEN 'Pilates + estiramientos'
    WHEN 6 THEN 'HIIT intenso'
    ELSE NULL
  END
FROM generate_series('2026-03-01'::date, '2026-05-16'::date, '1 day'::interval) d;

-- 5. BODY MEASUREMENTS - PABLO (cada ~12 días)
INSERT INTO body_measurements (user_id, measurement_date, chest_cm, waist_cm, hips_cm, arm_cm, thigh_cm, neck_cm) VALUES
  (1, '2026-03-01', 102.0, 88.0, 100.0, 36.5, 58.0, 40.0),
  (1, '2026-03-12', 101.5, 87.2, 99.5, 36.6, 57.8, 39.8),
  (1, '2026-03-24', 101.0, 86.5, 99.0, 36.7, 57.5, 39.7),
  (1, '2026-04-05', 100.5, 85.7, 98.5, 36.8, 57.2, 39.5),
  (1, '2026-04-17', 100.0, 85.0, 98.0, 36.9, 57.0, 39.4),
  (1, '2026-04-29', 99.5, 84.2, 97.5, 37.0, 56.7, 39.2),
  (1, '2026-05-11', 99.0, 83.5, 97.0, 37.1, 56.5, 39.1),
  (1, '2026-05-16', 98.8, 83.2, 96.8, 37.2, 56.3, 39.0);

-- 6. BODY MEASUREMENTS - ANA (cada ~12 días)
INSERT INTO body_measurements (user_id, measurement_date, chest_cm, waist_cm, hips_cm, arm_cm, thigh_cm, neck_cm) VALUES
  (2, '2026-03-01', 92.0, 72.0, 98.0, 28.5, 54.0, 34.0),
  (2, '2026-03-12', 91.8, 71.7, 97.8, 28.5, 53.9, 33.9),
  (2, '2026-03-24', 91.5, 71.3, 97.5, 28.6, 53.7, 33.8),
  (2, '2026-04-05', 91.3, 71.0, 97.3, 28.6, 53.6, 33.8),
  (2, '2026-04-17', 91.0, 70.7, 97.0, 28.7, 53.4, 33.7),
  (2, '2026-04-29', 90.8, 70.3, 96.8, 28.7, 53.3, 33.6),
  (2, '2026-05-11', 90.5, 70.0, 96.5, 28.8, 53.1, 33.6),
  (2, '2026-05-16', 90.4, 69.8, 96.4, 28.8, 53.0, 33.5);

-- 7. CHAT MESSAGES - PABLO
INSERT INTO chat_messages (user_id, role, content, created_at) VALUES
  (1, 'user', 'Hola, quiero empezar a hacer ejercicio. ¿Por dónde me recomiendas empezar?', '2026-03-01 09:15:00+01'),
  (1, 'assistant', '¡Hola Pablo! Te recomiendo empezar con una rutina equilibrada: 3 días de running a la semana y 2 de pesas. Así trabajas cardio y fuerza. ¿Cuál es tu objetivo principal?', '2026-03-01 09:15:10+01'),
  (1, 'user', 'Quiero bajar de peso y definir. Actualmente peso 85 kg.', '2026-03-01 09:20:00+01'),
  (1, 'assistant', 'Perfecto. Con un déficit calórico moderado (200-300 kcal/día) y ejercicio regular, puedes perder 0.5-1 kg por semana. Te sugiero: correr 3x/semana, pesas 2x/semana, y mantener proteínas altas.', '2026-03-01 09:20:15+01'),
  (1, 'user', '¿Cómo voy con mi progreso? Llevo un mes', '2026-04-01 18:00:00+02'),
  (1, 'assistant', '¡Muy bien Pablo! Has bajado de 85 a 84.2 kg y tu grasa corporal ha pasado de 22% a 21.5%. Vas por buen camino, sigue así.', '2026-04-01 18:00:15+02'),
  (1, 'user', 'Hoy hice mi mejor marca en 5K: 24:30', '2026-04-20 20:00:00+02'),
  (1, 'assistant', '¡Eso es increíble! 24:30 en 5K es un gran tiempo. Estás progresando muy bien tanto en cardio como en fuerza.', '2026-04-20 20:00:12+02'),
  (1, 'user', '¿Debo aumentar el déficit calórico? Llevo 2 meses y el ritmo ha bajado', '2026-05-01 12:00:00+02'),
  (1, 'assistant', 'Es normal que el ritmo se ralentice. No recomiendo aumentar el déficit, mejor añade un día de HIIT o aumenta la intensidad del running. Tu cuerpo se ha adaptado.', '2026-05-01 12:00:20+02');

-- 8. CHAT MESSAGES - ANA
INSERT INTO chat_messages (user_id, role, content, created_at) VALUES
  (2, 'user', 'Hola, quiero tonificar sin perder mucha curva', '2026-03-05 10:00:00+01'),
  (2, 'assistant', '¡Hola Ana! Para tonificar manteniendo curvas, enfócate en: pesas moderadas (8-12 reps), proteína suficiente, y nada de déficit agresivo. El Pilates y natación son excelentes complementos.', '2026-03-05 10:00:15+01'),
  (2, 'user', '¿Qué ejercicios me recomiendas para glúteos?', '2026-03-20 16:30:00+01'),
  (2, 'assistant', 'Sentadillas profundas, peso muerto rumano, hip thrusts y zancadas. 3-4 series de 10-12 reps con peso moderado-alto. Combínalo con Pilates 2x/semana para activación.', '2026-03-20 16:30:20+01'),
  (2, 'user', 'He notado más definición en brazos, ¿es normal?', '2026-04-15 18:45:00+02'),
  (2, 'assistant', '¡Sí! Tus medidas muestran que has ganado un poco de tono muscular en brazos (28.5→28.7 cm) mientras mantienes caderas. Es exactamente lo que buscabas. Sigue así.', '2026-04-15 18:45:18+02');

-- 9. USER CONFIGS
INSERT INTO user_configs (user_id, provider, base_url, default_model, created_at, updated_at) VALUES
  (1, 'OpenAI', 'https://api.openai.com/v1', 'gpt-4o-mini', '2026-03-01 09:00:00+01', '2026-03-01 09:00:00+01'),
  (2, 'OpenAI', 'https://api.openai.com/v1', 'gpt-4o-mini', '2026-03-05 09:30:00+01', '2026-03-05 09:30:00+01');
