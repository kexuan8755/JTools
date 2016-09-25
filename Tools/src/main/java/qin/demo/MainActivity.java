package qin.demo;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qin.tool.sql.DbDao;
import qin.tool.sql.DbDao.OnDbUpdateListener;
import qin.tool.sql.DbUtils;
import qin.tool.utils.FileUtils;
import qin.tool.utils.JSONUtils;
import qin.tool.utils.StringUtils;
import qin.tool.utils.TaskUtils;
import qin.tool.utils.ViewUtils;

public class MainActivity extends Activity implements View.OnClickListener{
	Handler mHandler = null;
	DbUtils utils = null;
	Person[] array = {
		new Person(1, "Lili", "13574315660"),
			new Person(2, "Limei", "13574315661"),
			new Person(3, "Lidan", "13574315662"),
			new Person(4, "Hanmeimei", "13574315663"),
			new Person(5, "Lijia", "13574315664"),
			new Person(6, "Guotai", "13574315665"),
			new Person(7, "ShaoYou", "13574315666"),
			new Person(8, "Wenjie", "13574315667"),
			new Person(9, "Wuxin", "13574315668"),
			new Person(10, "Qinye", "13574315669"),
	};

	int index = 0;

	public int getIndex() {
		return (index++) % array.length;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View root = findViewById(R.id.root);
		Log.e("db", "onCreate ===== > ");
		OnDbUpdateListener listener = new OnDbUpdateListener() {
			@Override
			public void onUpgrade(SQLiteDatabase database, int oldver, int newver) {
				
			}
		};
		DbDao dao = new DbDao(getApplicationContext(), listener);
		utils = DbUtils.getDbUtils(dao);
		for(int i = 1; i <= 24; i++) {
			final int id = getResources().getIdentifier("button" + i, "id", getPackageName());
			Log.e("db", "id = " + id);
			if(id >= 0) {
				ViewUtils.setOnClickListener(root,id, this);
			}
		}
	}

	@Override
	public void onClick(View v) {
		onExtraClick(v);
	}

	public void onExtraClick(View view) {
		final int vid = view.getId();
		switch (vid) {
			case R.id.button1:
				replace(array[getIndex()]);
				break;
			case R.id.button2:
				insert(array[getIndex()]);
				break;
			case R.id.button3:
				update(new Person(8, "Wenjie", "15874315667"));
				break;
			case R.id.button4:
				update(array[0], new Person(8, "Qinjie", "18674315667"));
				break;
			case R.id.button5:
				update(new Person(5, "Hehuan", "15874315667"), "id");
				break;
			case R.id.button6:
				delete();
				break;
			case R.id.button7:
				delete(array[getIndex()]);
				break;
			case R.id.button8:
				delete(new Person(7, null, null), "id");
				break;
			case R.id.button9:
				query();
				break;
			case R.id.button10:
				query(array[getIndex()]);
				break;
			case R.id.button11:
				query(array[getIndex()], "id");
				break;
			case R.id.button12:
				query(new String[]{"id", "name"});
				break;
			case R.id.button13:
				query("id = 5");
				break;
			case R.id.button14:
				query("name = ?", "Wenjie");
				break;
			case R.id.button15:
				query(null,false,null,null,new String[]{"id"},null,null,null,null,null);
				break;
			case R.id.button16:
				query(null,false,null,null,new String[]{"id"},"count(id) > 1",null,null,null,null);
				break;
			case R.id.button17:
				query(null,false,null,null,null,null,new String[]{"number"},null,null,null);
				break;
			case R.id.button18:
				query(null,false,null,null,new String[]{"id"},null,null,null,"3","3");
				break;
		}
	}

	public void replace(final Person person) {
		TaskUtils.asyncExec(new TaskUtils.Task<Person>() {
			@Override
			public void run() {
				utils.insertOrReplace(person);
			}
		});
	}

	public void insert(final Person person) {
		TaskUtils.asyncExec(new TaskUtils.Task<Person>() {
			@Override
			public void run() {
				utils.insertOrReplace(person);
			}
		});
	}

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

	void print(@Nullable List<Person> list) {
		if(list == null) return;
		for(Person p : list) {
			Log.e("db", p.toString());
		}
	}

	boolean detecting = false;
	public void loadfirstDb() {
		if(detecting) return;
		detecting = true;
		final long start = SystemClock.elapsedRealtime();
		try {
			String datas = FileUtils.readStringFromAssets(getApplicationContext(), R.raw.radio_freqs, null);
			if(datas == null) {
				detecting = false;
				return;
			}
			boolean load = false;
			int version = 0;
			List<Station> list = new ArrayList<Station>();
			if(!StringUtils.isEmpty(datas)) {
				JSONObject root = new JSONObject(datas);
				if(root != null && root.has("radio")) {
					if(root.has("version")) {
						version = JSONUtils.getInt(root, "version", 0);
					}
					
					JSONArray array = root.getJSONArray("radio");
					final int count  = array == null ? 0 : array.length();
					Log.e("dbx", "total count  = " + count);
					for(int i = 0; i < count; i++) {
						JSONObject json = array.getJSONObject(i);
						if(json != null) {
							final String city = JSONUtils.getString(json, "cityName", "");
							final String title = JSONUtils.getString(json, "freqName", "");
							final int freq = JSONUtils.getInt(json, "freqNum", 0);
							Station station = 
									new Station(1, 0, title, freq, 2);
							station.setCity(city);
							station.setOnline(1);
//							utils.insertOrReplace(station);
							if(!list.contains(station)) {
								list.add(station);
							}
							if(!load && station != null) load = true;
						}
					}
					if(list != null && list.size() > 0) {
						utils.insertList(true, list);
					}
				}
			}
			Log.e("db", "total time  = " + (SystemClock.elapsedRealtime() - start));
			if(load) {}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mHandler == null) {
			HandlerThread work = new HandlerThread("test db");
			work.start();
			mHandler = new Handler(work.getLooper());
		}
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				loadfirstDb();
			}
		});
	}
	
	@Override
	protected void onPause() {
		if(mHandler != null) {
			mHandler.getLooper().quit();
			mHandler = null;
		}
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
