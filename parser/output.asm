# This program contains MIPS code auto-generated from given PASCAL code.
# @author Juliana Li
# @version 1/8/24
# (:
	.data
newLine: .asciiz "\n"

	varx: .word 0
	varignore: .word 0
	.text
.globl main
main: #QTSPIM will automatically look for main

	subu $sp $sp 4	# push to stack
sw $ra ($sp)
	li $v0 0    #return val
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	li $v0 2
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	li $v0 4
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	jal proccountUp
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $ra ($sp)	# pop from stack
addu $sp $sp 4 
	la $t0 varx	# variable assignment
sw $v0 ($t0)
	li $v0 10
syscall # halt
	proccountUp:

	lw $v0 4($sp)	# get local var count
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	lw $v0 4($sp)	# get local var max
	lw $t0 ($sp)	# pop from stack
addu $sp $sp 4 
	bgt $t0 $v0 endif1	# if statement
	lw $v0 4($sp)	# get local var count
	move $a0 $v0
li $v0 1	 # print number 
syscall
	la $a0 newLine
li $v0 4
syscall
	subu $sp $sp 4	# push to stack
sw $ra ($sp)
	li $v0 0    #return val
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	lw $v0 12($sp)	# get local var count
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)	# pop from stack
addu $sp $sp 4 
	addu $v0 $t0 $v0
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	lw $v0 12($sp)	# get local var max
	subu $sp $sp 4	# push to stack
sw $v0 ($sp)
	jal proccountUp
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $v0 ($sp)	# pop from stack
addu $sp $sp 4 
	lw $ra ($sp)	# pop from stack
addu $sp $sp 4 
	la $t0 varignore	# variable assignment
sw $v0 ($t0)
	j else1
endif1:
else1:
	jr $ra
