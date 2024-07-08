#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/ioport.h>
#include <asm/errno.h>
#include <asm/io.h>
#include <linux/fs.h>
#include <linux/proc_fs.h>
#include <linux/uaccess.h>
#include <asm/io.h>



MODULE_INFO(intree, "Y");
MODULE_LICENSE("GPL");
MODULE_AUTHOR("Karol Żelazowski");
MODULE_DESCRIPTION("Simple kernel module for SYKOM lecture");
MODULE_VERSION("0.01");


#define SYKT_GPIO_BASE_ADDR (0x00100000)
#define SYKT_GPIO_SIZE (0x8000)
#define SYKT_EXIT (0x3333)
#define SYKT_EXIT_CODE (0x7F)

#define A_REG_GPIO_ADDR_OFFSET (0x288)
#define S_REG_GPIO_ADDR_OFFSET (0x2A0)
#define W_REG_GPIO_ADDR_OFFSET (0x298)


#define LEN 32

void __iomem *baseptrA;
void __iomem *baseptrS;
void __iomem *baseptrW;
void __iomem *baseptr;

static struct proc_dir_entry *direc;

static struct proc_dir_entry *proc_file_rej_a;
static struct proc_dir_entry *proc_file_rej_s;
static struct proc_dir_entry *proc_file_rej_w;



static ssize_t my_read_A(struct file *file, char __user *ubuf, size_t len, loff_t *off) {
	printk(KERN_INFO "Number of prime numbers having been searched\n");
	char buf[LEN];
	int not_copied, to_copy, num;
	if(*off > 0) {
        return 0;
        }
	num = readl(baseptrA);
	snprintf(buf,LEN, "%o\n", num);
	to_copy = strlen(buf);
	if (to_copy > LEN){
		to_copy = LEN;
	}

	not_copied = copy_to_user(ubuf, buf, to_copy);
	if(not_copied){
		printk(KERN_ERR "Error copying data to user space\n");
		return -EFAULT;
	}

	*off = to_copy;
        return to_copy;
}

static ssize_t my_write_A(struct file *file, const char __user *ubuf, size_t count, loff_t *off) {
	int num, i;
	char buf[LEN];

	if(*off>0 || count>LEN)
	{
		printk(KERN_ERR "Invalid offset or count exceeds buffer length\n");
		return -EPERM;
	}
	if(copy_from_user(buf, ubuf, count))
	{
		printk(KERN_ERR "Error copying data from user space\n");
		return -EFAULT;
	}
	num=sscanf(buf, "%o", &i);
	if(num!=1)	{
		printk(KERN_ERR "Error parsing value from user input: %s\n", buf);
		return -EFAULT;
	}
	if( i <= 01 || i > 01750){
		printk(KERN_ERR "Error: Ivalid value of argument: Try between 0d0 and 0d1000.\n");
	}

	writel(i, baseptrA);
	return count;

}
static const struct file_operations A_ops = {
	.owner = THIS_MODULE,
	.read = my_read_A,
	.write = my_write_A
};

static ssize_t my_read_S(struct file *file,  char __user *ubuf, size_t count, loff_t *off){
 printk(KERN_INFO "Reading status of the device\n");
    char buf[LEN];
    int to_copy, not_copied, num;

    if (*off > 0) {
        return 0;
    }

    num = readl(baseptrS);
    snprintf(buf, LEN, "%o\n", num);
    to_copy = strlen(buf);

    if (to_copy > LEN) {
        to_copy = LEN;
    }

    not_copied = copy_to_user(ubuf, buf, to_copy);
    if (not_copied) {
        printk(KERN_ERR "Error copying data to user space\n");
        return -EFAULT;
    }

    *off = to_copy;
    return to_copy;
}

static const struct file_operations S_ops = {
	.owner = THIS_MODULE,
	.read = my_read_S
};



static ssize_t my_read_W(struct file *file,  char __user *ubuf, size_t count, loff_t *off){
	 printk(KERN_INFO "Reading result of the computing:\n");
    char buf[LEN];
    int to_copy, not_copied, num;

    if (*off > 0) {
        return 0;
    }

    num = readl(baseptrW);
    snprintf(buf, LEN, "%o\n", num);
    to_copy = strlen(buf);

    if (to_copy > LEN) {
        to_copy = LEN;
    }

    not_copied = copy_to_user(ubuf, buf, to_copy);
    if (not_copied) {
        printk(KERN_ERR "Error copying data to user space\n");
        return -EFAULT;
    }

    *off = to_copy;
    return to_copy;
}

static const struct file_operations W_ops = {
	.owner = THIS_MODULE,
	.read = my_read_W
};

int my_init_module(void){
	printk(KERN_INFO "Init my module.\n");
	baseptr=ioremap(SYKT_GPIO_BASE_ADDR, SYKT_GPIO_SIZE);
	
	if(baseptr == NULL){
		printk("Faild to map GPIO memory!\n");
		return -ENOMEM;
	}

	baseptrA = baseptr + A_REG_GPIO_ADDR_OFFSET;
	baseptrS = baseptr + S_REG_GPIO_ADDR_OFFSET;
	baseptrW = baseptr + W_REG_GPIO_ADDR_OFFSET;
	
	if(baseptrA == NULL || baseptrS == NULL || baseptrW == NULL ){
		printk("Failed to map registers memmory!\n");
		return -ENOMEM;
	}
	direc = proc_mkdir("proj4zelkar",NULL);
	if(!direc){
		printk(KERN_INFO "Error unable to creat procfs directory!\n");
		proc_remove(direc);
		return -ENOMEM;
	}
	proc_file_rej_a = proc_create("rejA", 0666, direc, &A_ops);
	proc_file_rej_s = proc_create("rejS", 0444, direc, &S_ops);
	proc_file_rej_w = proc_create("rejW", 0444, direc, &W_ops);
	if(!proc_file_rej_a  || !proc_file_rej_s  || !proc_file_rej_w){
		printk(KERN_INFO "Error unable to creat procfs files!\n");
		proc_remove(proc_file_rej_a);
		proc_remove(proc_file_rej_s);
		proc_remove(proc_file_rej_w);
		proc_remove(direc);
	}
	return 0;
}
void my_cleanup_module(void){
	printk(KERN_INFO "Cleanup my module.\n");
	proc_remove(proc_file_rej_a);
	proc_remove(proc_file_rej_s);
	proc_remove(proc_file_rej_w);
	remove_proc_entry("proj4karzel",NULL);
	writel(SYKT_EXIT | ((SYKT_EXIT_CODE)<<16), baseptr);
	if (baseptrA) {
        iounmap(baseptrA);
    }
	if (baseptrS) {
        iounmap(baseptrS);
    }
	if (baseptrW) {
        iounmap(baseptrW);
    }
	iounmap(baseptr);
}
module_init(my_init_module);
module_exit(my_cleanup_module);
