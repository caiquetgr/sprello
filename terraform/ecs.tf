
resource "template_file" "ecs_task_definition_file" {
    template = "${file("./ecs-task.json")}"
    
    vars = {
        db_url = module.db.db_jdbc_connection_string
        db_username = aws_db_instance.db.username
        db_password = "${var.db_password}"
        task_role_arn = aws_iam_role.ecs_task_role.arn
        execution_role_arn = aws_iam_role.ecs_task_execution_role.arn
    }

    depends_on = [
      aws_db_instance.db,
      aws_iam_role.ecs_task_role,
      aws_iam_role.ecs_task_execution_role
    ]
}

resource "aws_iam_role" "ecs_task_role" {
    name = "${var.project_name}_db_policy"
    path = "/"
    description = "DB policy"

    assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "rds-data:ExecuteStatement",
                "rds-db:connect",
                "rds-data:RollbackTransaction",
                "rds-data:CommitTransaction",
                "rds-data:ExecuteSql",
                "rds-data:BatchExecuteStatement",
                "rds-data:BeginTransaction"
            ],
            "Resource": [
                "arn:aws:rds-db:sa-east-1:317811791764:dbuser:${aws_db_instance.db.resource_id}/sprello",
                "arn:aws:rds:sa-east-1:317811791764:cluster:${aws_db_instance.db.identifier}"
            ]
        }
    ]})

}

resource "aws_iam_role" "ecs_task_execution_role" {
    name = "${var.project_name}_ecs_task_execution_policy"
    path = "/"
    description = "Task executions policy"

    assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:BatchGetImage",
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": "*"
        }
    ]})
}

resource "aws_ecs_cluster" "ecs_cluster" {
    name = "${var.project_name}-cluster"

    configuration {
        execute_command_configuration {
            kms_key_id = "key-0ccdab1cc1739bff6"
        }  
    }
    
}


resource "aws_ecs_service" "ecs_service" {
    name = "${var.project_name}-service"
    cluster = aws_ecs_cluster.ecs_cluster.id
    task_definition = aws_ecs_task_definition.ecs_task_definition.arn
    desired_count = 2

    load_balancer {
      target_group_arn = aws_alb_target_group.alb_target_group.arn
      container_name = "sprello"
      container_port = 8080
    }

    network_configuration {
      subnets = [aws_subnet.public_subnet.id]
    }
}

resource "aws_ecs_task_definition" "ecs_task_definition" {
  family                = "${var.project_name}-task"
  container_definitions = template_file.ecs_task_definition_file.rendered
}