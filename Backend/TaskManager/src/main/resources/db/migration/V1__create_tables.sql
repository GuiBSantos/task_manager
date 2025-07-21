CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       role VARCHAR(20) NOT NULL, -- ADMIN, GERENTE, MEMBRO
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teams (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(100) NOT NULL UNIQUE,
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE team_members (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              team_id UUID NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
                              user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              UNIQUE (team_id, user_id)
);

CREATE TABLE tasks (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       title VARCHAR(200) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) NOT NULL,
                       assigned_to UUID REFERENCES users(id),
                       team_id UUID NOT NULL REFERENCES teams(id),
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task_status_history (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
                                     old_status VARCHAR(20),
                                     new_status VARCHAR(20) NOT NULL,
                                     changed_by UUID REFERENCES users(id),
                                     changed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tasks_team_id ON tasks(team_id);
CREATE INDEX idx_tasks_assigned_to ON tasks(assigned_to);
CREATE INDEX idx_task_status_history_task_id ON task_status_history(task_id);

