package com.example.kursach.adapter;

//public  class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
//
//   Context context;
//   List<Course> courses;
//
//    public CourseAdapter(Context context, List<Course> courses) {
//        this.context = context;
//        this.courses = courses;
//    }
//
//    @NonNull
//    @Override
//    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View courseItems = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
//        return new CourseAdapter.CourseViewHolder(courseItems);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
//
//        holder.courseBg.setBackgroundColor(Color.parseColor(courses.get(position).getColor()));
//
//        int imageId = context.getResources().getIdentifier("ic_" + courses.get(position).getImg(), "drawable", context.getPackageName());
//        holder.courseImage.setImageResource(imageId);
//
//        holder.courseMoney.setText(courses.get(position).getMoney());
//        holder.courseModel.setText(courses.get(position).getModel());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return courses.size();
//    }
//
//    public static final class CourseViewHolder extends RecyclerView.ViewHolder{
//
//        LinearLayout courseBg;
//        ImageView courseImage;
//        TextView courseMoney,courseModel;
//
//
//        public CourseViewHolder(@NonNull View itemView) {
//
//            super(itemView);
//
//            courseBg = itemView.findViewById(R.id.courseBg);
//            courseImage = itemView.findViewById(R.id.imageView);
//            courseMoney = itemView.findViewById(R.id.textMoney);
//            courseModel = itemView.findViewById(R.id.textModel);
//
//        }
//    }
//}
