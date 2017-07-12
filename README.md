# Numercial image


Pipeline processing on image.


Example
=======

-Defined your pipeline.yaml file :

```

#======  Pipeline configuration ======

pipeline:
    - label: 'first step'  
      type: scale
    - label: 'second step' 
      type: border
```

-Then execute the command on your image, here "cat.jpg" in directory ".../cat_border" :
numericalimage -w /Users/user/Documents/numericalImage/cat_border -i cat.jpg -v

See the result in the same directory -> "transform_xxxx.png"

How it works ?
==============

There is a default configuration for all transformations defined in the "default_configuration.yaml".
You can override this configuration in your own file configuration. 

```

#=======  Default configuration =====


blacktowhite:
    pix_size: 10
    
scale:
    scale_factor: 3
    
border:
    canny_edge_detector:
        low_threshold: 3.0
        high_threshold: 3.5
        gaussian_kernel_radius: 3.0
        gaussian_kernel_width: 16
        contrast_normalized: false

reduce:
    pix_size: 10
    image_type: byte_gray
    
determine_lines:
    pix_size: 1
    neutral_color: rgb(170, 0, 0)
    color_to_treat: rgb(0, 0, 0)
    line_max_size: 12

determine_lines_multi_color:
    neutral_color: rgb(170, 0, 0)
    line_max_size: 12
    
quantize:
    nb_colors: 9
      
convert_to_html:
    multicolor: false

```
    
In previous example, we can override the scale default configuration with this configuration :

```

pipeline:
    - label: 'first step'  
      type: scale
      scale_factor: 8

```

Easy !


Change your image in a pixel game
=================================


```

####### NumercialImage Configuration #######

#====  Pipeline configuration ====


pipeline:
    - label: 'first step'  
      type: reduce
      pix_size: 21
      image_type: int_rgb
    - label: 'second step' 
      type: quantize
    - label: 'first step'  
      type: determine_lines_multi_color
    - label: 'second step' 
      type: convert_to_html
      multicolor: true
      
```



