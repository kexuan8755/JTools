# JTools
JTools for android 
一个数据库工具框架和一些常用的方法整理

DbUtils:
数据列类型

	public enum ColumnType {
		COLUMN,//普通数据列
		PRIMARY,//主键值
		UNIQUE, //数据列唯一限制
		FOREIGN,//外健限制
		EQUALS   //对象相等默认的比较数据
	}
对于数据列定义通常会有Primary、Unique、Foreign，很多时候我们用不上这些定义，当时又想有默认的比对条件所以引入了Equals类型；Column只是一个普通行。

这样定义一个数据表的信息，当使用DbUtils操作增删改查时将自动引入数据库操作

	@Table
	public class Person {
		@Column(notNull=true)
		int id;
		@Column(type=ColumnType.UNIQUE)
		String name;
		@Column
		String number;
		public Person(){}
		public Person(int id, String name, String number){
			this.id = id;
			this.name = name;
			this.number = number;
		}
		……
	}

添加或者替换

	public void replace(final Person person) {
		TaskUtils.asyncExec(new TaskUtils.Task<Person>() {
			@Override
			public void run() {
				utils.insertOrReplace(person);
			}
		});
	}

添加

	public void insert(final Person person) {
		TaskUtils.asyncExec(new TaskUtils.Task<Person>() {
			@Override
			public void run() {
				utils.insertOrReplace(person);
			}
		});
	}

更新

	public void update(final Person old, final Person np) {
		TaskUtils.asyncExec(new TaskUtils.Task<Void>() {
			@Override
			public void run() {
				utils.update(old, np);
			}
		});
	}

	public void update(final Person person, final String...columns) {
		TaskUtils.asyncExec(new TaskUtils.Task<Void>() {
			@Override
			public void run() {
				utils.update(person, columns);
			}
		});
	}
	
删除

	public void delete() {
		TaskUtils.asyncExec(new TaskUtils.Task<Void>() {
			@Override
			public void run() {
				utils.delete(Person.class);
			}
		});
	}

	public void delete(final Person person, final String...columns) {
		TaskUtils.asyncExec(new TaskUtils.Task<Void>() {
			@Override
			public void run() {
				utils.delete(person, columns);
			}
		});
	}

查询

	public void query() {
		TaskUtils.asyncExec(new TaskUtils.Task<List<Person>>() {
			@Override
			public void run() {
				data = utils.query(Person.class);
			}

			@Override
			public void finish(List<Person> data) {
				print(data);
			}
		});
	}

	public void query(final Person person, final String...columns) {
		TaskUtils.asyncExec(new TaskUtils.Task<List<Person>>() {
			@Override
			public void run() {
				data = utils.query(person, true, columns);
			}

			@Override
			public void finish(List<Person> data) {
				print(data);
			}
		});
	}

	public void query(@NonNull final String[] columns) {
		TaskUtils.asyncExec(new TaskUtils.Task<List<Person>>() {
			@Override
			public void run() {
				data = utils.query(Person.class, true, columns);
			}

			@Override
			public void finish(List<Person> data) {
				print(data);
			}
		});
	}

	public void query(final String selection, final String...args) {
		query(null,true,selection,args,null,null,null,null,null,null);
	}

	public void query(final String[] columnNames, final boolean distinct, final String selection, final String[] selectArgs,
					  final String [] groupByColumns, final String having, final String [] orderByColumns, final String orderBy, final String limit, final String offset) {
		TaskUtils.asyncExec(new TaskUtils.Task<List<Person>>() {
			@Override
			public void run() {
				data = utils.query(Person.class,columnNames,distinct,selection,selectArgs,groupByColumns,having,orderByColumns,orderBy,limit,offset);
			}

			@Override
			public void finish(List<Person> data) {
				print(data);
			}
		});
	}
