resource "aws_alb" "alb" {  
  name            = "${var.project_name}-alb"  
  subnets         = [aws_subnet.public_subnet.id]
  security_groups = aws_security_group.alb_security_group.id

  tags = {    
    Name    = "${var.project_name}_alb" 
  }   
}

resource "aws_alb_listener" "alb_listener" {
  load_balancer_arn = aws_alb.alb.arn

  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.alb_target_group.arn
  }
}

resource "aws_alb_target_group" "alb_target_group" {  
  name     = "${var.project_name}-alb-target-group" 
  port     = "8080"  
  protocol = "HTTP"  
  vpc_id   = aws_vpc.vpc.id
  
  tags = {
    name = "${var.project_name}-alb-target-group" 
  }   

}