#!/usr/bin/env bash
# new-java.sh — crea un archivo .java con package, imports y clase skeleton
# Uso: ./scripts/new-java.sh [tipo] NombreClase
# Tipos: class (default), service, controller, entity, repository, dto, config
# Ej:    ./scripts/new-java.sh service UserService
#        ./scripts/new-java.sh entity User
#        ./scripts/new-java.sh controller DailyRecordController

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_BASE="$(cd "$SCRIPT_DIR/../src/main/java" && pwd)"
BASE_PKG="com.healthtracker.backend"

usage() {
    echo "Uso: $(basename "$0") [tipo] NombreClase"
    echo "  tipos: class, service, controller, entity, repository, dto, config"
    echo ""
    echo "Ejemplos:"
    echo "  $(basename "$0") service UserService"
    echo "  $(basename "$0") entity DailyRecord"
    echo "  $(basename "$0") controller DashboardController"
    exit 1
}

# Detecta el package desde el directorio actual
detect_package() {
    local current_dir="$(pwd)"
    if [[ "$current_dir" == "$SRC_BASE"* ]]; then
        local rel="${current_dir#$SRC_BASE/}"
        rel="${rel%/}"
        rel="${rel////.}"
        echo "$rel"
    else
        echo "$BASE_PKG"
    fi
}

TYPE="${1:-class}"
if [[ "$TYPE" =~ ^- ]]; then
    TYPE="class"
    NAME="${1#-}"
else
    NAME="${2:-}"
fi

if [[ -z "$NAME" ]]; then
    usage
fi

PACKAGE="$(detect_package)"
FILE="${NAME}.java"

if [[ -f "$FILE" ]]; then
    echo "Error: $FILE ya existe"
    exit 1
fi

generate_class() {
    cat <<EOF
package ${PACKAGE};

public class ${NAME} {

    public ${NAME}() {
    }

}
EOF
}

generate_service() {
    cat <<EOF
package ${PACKAGE};

import org.springframework.stereotype.Service;

@Service
public class ${NAME} {

}
EOF
}

generate_controller() {
    local base_name="${NAME%Controller}"
    local path="/api/v1/${base_name,,}s"
    cat <<EOF
package ${PACKAGE};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path}")
public class ${NAME} {

}
EOF
}

generate_entity() {
    cat <<EOF
package ${PACKAGE};

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_name")
public class ${NAME} {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ${NAME}() {
    }

    public Long getId() {
        return id;
    }

}
EOF
}

generate_repository() {
    # Asume que la entidad asociada tiene el mismo nombre sin "Repository"
    local entity="${NAME%Repository}"
    cat <<EOF
package ${PACKAGE};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${NAME} extends JpaRepository<${entity}, Long> {

}
EOF
}

generate_dto() {
    cat <<EOF
package ${PACKAGE};

public record ${NAME}(

) {
}
EOF
}

generate_config() {
    cat <<EOF
package ${PACKAGE};

import org.springframework.context.annotation.Configuration;

@Configuration
public class ${NAME} {

}
EOF
}

case "$TYPE" in
    service)    generate_service > "$FILE" ;;
    controller) generate_controller > "$FILE" ;;
    entity)     generate_entity > "$FILE" ;;
    repository) generate_repository > "$FILE" ;;
    dto)        generate_dto > "$FILE" ;;
    config)     generate_config > "$FILE" ;;
    class)      generate_class > "$FILE" ;;
    *)          usage ;;
esac

echo "Creado: $FILE (package: ${PACKAGE})"
